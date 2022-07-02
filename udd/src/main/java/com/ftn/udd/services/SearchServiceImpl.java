package com.ftn.udd.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ftn.udd.dto.JobApplicationDTO;
import com.ftn.udd.elasticsearch.dto.ResultsDTO;
import com.ftn.udd.elasticsearch.dto.SearchParamDTO;
import com.ftn.udd.elasticsearch.enumeration.SearchType;
import com.ftn.udd.elasticsearch.mappers.IndexingUnitMapper;
import com.ftn.udd.elasticsearch.model.CVIndexingUnit;
import com.ftn.udd.elasticsearch.model.IndexingUnit;
import com.ftn.udd.elasticsearch.repository.CVIndexingUnitRepository;
import com.ftn.udd.elasticsearch.repository.IndexingUnitRepository;
import com.ftn.udd.model.JobApplication;
import com.ftn.udd.repositories.IJobApplicationRepository;
import com.ftn.udd.utils.PdfHandler;
import com.ftn.udd.utils.PdfUtils;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private IJobApplicationRepository jaRepo;

	@Autowired
	private GeoPointService geoPointService;

	@Autowired
	private IndexingUnitRepository indexingUnitRepository;

	@Autowired
	private CVIndexingUnitRepository cvRepo;

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class.getName());

	@Override
	@Async("processExecutor")
	public String index(JobApplicationDTO jobApplication) throws Exception {
		MultipartFile cv = jobApplication.getCv();

		if (!cv.isEmpty()) {
			String pathCV = PdfUtils.upload(cv);
			if (pathCV != null) {
				PdfHandler pdfHandler = new PdfHandler();
				String text = pdfHandler.getText(new File(pathCV));

				CVIndexingUnit cvui = new CVIndexingUnit();
				cvui.setContent(text);

				cvui.setPath(pathCV);
				cvRepo.save(cvui);

				JobApplication application = new JobApplication(null, jobApplication.getFirstName(),
						jobApplication.getLastName(), jobApplication.getEmail(), jobApplication.getEducationDegree(),
						jobApplication.getCity(), jobApplication.getCountry(), pathCV);
				application = jaRepo.save(application);

				String basicInfo = jobApplication.getFirstName() + " " + jobApplication.getLastName() + ", "
						+ jobApplication.getEmail() + ", " + jobApplication.getEducationDegree() + " (education degree), "
						+ jobApplication.getCity() + ", " + jobApplication.getCountry();

				GeoPoint geoPoint = geoPointService.getGeoPoint(jobApplication.getCity(), jobApplication.getCountry());

				if (geoPoint == null) {
					throw new Exception("Non existing place");
				}

				IndexingUnit iu = new IndexingUnit(application.getId(), jobApplication.getFirstName(),
						jobApplication.getLastName(), jobApplication.getEducationDegree(), basicInfo, cvui, geoPoint);
				indexingUnitRepository.index(iu);

				logger.info("Successfully uploaded job application");
				return "Successfully uploaded job application";
			}
		} else {
			throw new Exception("CV must not be empty");
		}
		return null;
	}

	@Override
	public ResultsDTO advancedQuery(List<SearchParamDTO> searchParams, int page, int size) {
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		BoolQueryBuilder queryParams = QueryBuilders.boolQuery();

		for (SearchParamDTO searchParam : searchParams) {
			if (searchParam.getAttributeName().equals("cv")) {
				buildNestedParam(queryParams, searchParam.getPhraseQuery(), searchParam.getAttributeName(),
						searchParam.getSearchValue(), searchParam.getType());
			} else if (searchParam.getAttributeName().equals("educationDegree")) {
				buildRangeParam(queryParams, searchParam.getSearchValue(), searchParam.getSearchValue2(),
						searchParam.getType());
			} else {
				buildQueryParam(queryParams, searchParam.getPhraseQuery(), searchParam.getAttributeName(),
						searchParam.getSearchValue(), searchParam.getType());
			}
		}
		IndexingUnitMapper mapper = new IndexingUnitMapper();
		AggregatedPage<IndexingUnit> i = elasticsearchTemplate.queryForPage(
				createSearchQuery(nativeSearchQueryBuilder, queryParams, page, size), IndexingUnit.class, mapper);

		if (i == null) {
			return new ResultsDTO(new ArrayList<IndexingUnit>(), 0);
		}
		return new ResultsDTO(i.getContent(), mapper.totalElements);
	}

	@Override
	public ResultsDTO simpleQuery(String searchValue, int page, int size) {

		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		BoolQueryBuilder queryParams = new BoolQueryBuilder();

		queryParams.should(QueryBuilders.commonTermsQuery("firstName", searchValue));
		queryParams.should(QueryBuilders.commonTermsQuery("lastName", searchValue));
		queryParams.should(QueryBuilders.commonTermsQuery("educationDegree", searchValue));

		BoolQueryBuilder boolQueryCv = QueryBuilders.boolQuery();
		NestedQueryBuilder nestedQueryCv = QueryBuilders.nestedQuery("cv",
				boolQueryCv.should(QueryBuilders.commonTermsQuery("cv.content", searchValue)), ScoreMode.Total);

		queryParams.should(nestedQueryCv);

		IndexingUnitMapper mapper = new IndexingUnitMapper();
		AggregatedPage<IndexingUnit> i = elasticsearchTemplate.queryForPage(
				createSearchQuery(nativeSearchQueryBuilder, queryParams, page, size), IndexingUnit.class, mapper);

		if (i == null) {
			return new ResultsDTO(new ArrayList<IndexingUnit>(), 0);
		}
		return new ResultsDTO(i.getContent(), mapper.totalElements);
	}

	private void buildQueryParam(BoolQueryBuilder queryParams, Boolean isPhraseQuery, String attributeName,
			String searchValue, SearchType type) {
		if (isPhraseQuery) {
			if (type.equals(SearchType.AND)) {
				queryParams.must(QueryBuilders.matchPhraseQuery(attributeName, searchValue));
			} else if (type.equals(SearchType.OR)) {
				queryParams.should(QueryBuilders.matchPhraseQuery(attributeName, searchValue));
			}
		} else {
			if (type.equals(SearchType.AND)) {
				queryParams.must(QueryBuilders.commonTermsQuery(attributeName, searchValue));
			} else if (type.equals(SearchType.OR)) {
				queryParams.should(QueryBuilders.commonTermsQuery(attributeName, searchValue));
			}
		}

	}

	private void buildRangeParam(BoolQueryBuilder queryParams, String value1, String value2, SearchType type) {
		if (type.equals(SearchType.AND)) {
			queryParams.must(QueryBuilders.rangeQuery("educationDegree").from(value1).to(value2));
		} else if (type.equals(SearchType.OR)) {
			queryParams.should(QueryBuilders.rangeQuery("educationDegree").from(value1).to(value2));
		}
	}

	private void buildNestedParam(BoolQueryBuilder queryParams, Boolean isPhraseQuery, String searchName,
			String searchValue, SearchType type) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		NestedQueryBuilder nestedQuery;
		String searchNameContent = searchName + ".content";

		if (isPhraseQuery) {
			if (type.equals(SearchType.AND)) {
				nestedQuery = QueryBuilders.nestedQuery(searchName,
						boolQuery.must(QueryBuilders.matchPhraseQuery(searchNameContent, searchValue)), ScoreMode.None);
				queryParams.must(nestedQuery);
			} else if (type.equals(SearchType.OR)) {
				nestedQuery = QueryBuilders.nestedQuery(searchName,
						boolQuery.should(QueryBuilders.matchPhraseQuery(searchNameContent, searchValue)),
						ScoreMode.None);
				queryParams.should(nestedQuery);
			}
		} else {
			if (type.equals(SearchType.AND)) {
				nestedQuery = QueryBuilders.nestedQuery(searchName,
						boolQuery.must(QueryBuilders.commonTermsQuery(searchNameContent, searchValue)), ScoreMode.None);
				queryParams.must(nestedQuery);
			} else if (type.equals(SearchType.OR)) {
				nestedQuery = QueryBuilders.nestedQuery(searchName,
						boolQuery.should(QueryBuilders.commonTermsQuery(searchNameContent, searchValue)),
						ScoreMode.None);
				queryParams.should(nestedQuery);
			}
		}
	}

	private SearchQuery createSearchQuery(NativeSearchQueryBuilder searchQueryBuilder, BoolQueryBuilder queryParams,
			int page, int size) {
		return searchQueryBuilder
				.withQuery(queryParams).withHighlightFields(new HighlightBuilder.Field("cv.content").preTags("<b>")
						.postTags("</b>").numOfFragments(1).fragmentSize(500))
				.withPageable(PageRequest.of(page, size)).build();
	}

	@Override
	public ResultsDTO geoSearch(String place, int radius, int page, int size) throws Exception {
		GeoPoint gp = geoPointService.getGeoPoint(place, "");
		QueryBuilder filter = QueryBuilders.geoDistanceQuery("geoPoint").point(gp.getLat(), gp.getLon())
				.distance(radius, DistanceUnit.KILOMETERS);

		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.must(filter);

		IndexingUnitMapper mapper = new IndexingUnitMapper();
		AggregatedPage<IndexingUnit> i = elasticsearchTemplate.queryForPage(
				createSearchQuery(nativeSearchQueryBuilder, boolQuery, page, size), IndexingUnit.class, mapper);

		if (i == null) {
			return new ResultsDTO(new ArrayList<IndexingUnit>(), 0);
		}
		return new ResultsDTO(i.getContent(), mapper.totalElements);
	}

}
