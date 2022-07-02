package com.ftn.udd.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ftn.udd.elasticsearch.model.CVIndexingUnit;

public interface CVIndexingUnitRepository extends ElasticsearchRepository<CVIndexingUnit, Long> {

}
