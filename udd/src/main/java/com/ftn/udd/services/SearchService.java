package com.ftn.udd.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ftn.udd.dto.JobApplicationDTO;
import com.ftn.udd.elasticsearch.dto.ResultsDTO;
import com.ftn.udd.elasticsearch.dto.SearchParamDTO;
import com.ftn.udd.elasticsearch.model.IndexingUnit;

public interface SearchService {

	String index(JobApplicationDTO jobApplication) throws Exception;

	ResultsDTO advancedQuery(List<SearchParamDTO> searchParams, int page, int size);

	ResultsDTO simpleQuery(String searchValue, int page, int size);

	ResultsDTO geoSearch(String place, int radius, int page, int size) throws Exception;

}
