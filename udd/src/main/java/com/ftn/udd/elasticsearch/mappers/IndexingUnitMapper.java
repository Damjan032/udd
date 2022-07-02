package com.ftn.udd.elasticsearch.mappers;

import com.ftn.udd.elasticsearch.model.CVIndexingUnit;
import com.ftn.udd.elasticsearch.model.IndexingUnit;
import com.google.gson.Gson;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class IndexingUnitMapper implements SearchResultMapper {

	public Integer totalElements;

	@Override
	public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
		List<IndexingUnit> result = new ArrayList<>();

		for (SearchHit searchHit : searchResponse.getHits()) {
			if (searchResponse.getHits().getHits().length <= 0) {
				return null;
			}

			Gson gson = new Gson();
			Map<String, Object> source = searchHit.getSourceAsMap();

			@SuppressWarnings("unchecked")
			Map<String, Object> cvMap = gson.fromJson(gson.toJson(source.get("cv")), Map.class);

			CVIndexingUnit cv = new CVIndexingUnit(Math.round((double) cvMap.get("id")), (String) cvMap.get("content"),
					(String) cvMap.get("path"));

			String highValueCv;
			try {
				highValueCv = "..." + searchHit.getHighlightFields().get("cv.content").fragments()[0].toString()
						.replaceAll("[\\n\\r\\t]+", " ").trim().replaceAll(" +", " ") + "...";
			} catch (Exception e) {
				String[] words = cv.getContent().replaceAll("[\\n\\r\\t]+", " ").trim().replaceAll(" +", " ")
						.split(" ");
				if (words.length > 35) {
					highValueCv = String.join(" ", Arrays.copyOfRange(words, 0, 34));
				} else {
					highValueCv = String.join(" ", Arrays.copyOfRange(words, 0, words.length));
				}
			}

			cv.setContent(highValueCv);

			IndexingUnit indexingUnit = new IndexingUnit(Long.valueOf(searchHit.getId()),
					(String) source.get("firstName"), (String) source.get("lastName"),
					(String) source.get("educationDegree").toString(), (String) source.get("basicInfo"), cv, null);

			result.add(indexingUnit);
		}
		if (result.size() > 0) {
			this.totalElements = (int) searchResponse.getHits().getTotalHits();
			return new AggregatedPageImpl(result);
		}

		return null;
	}

	public IndexingUnitMapper() {
		this.totalElements = 0;
	}

}
