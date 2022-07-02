package com.ftn.udd.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ftn.udd.elasticsearch.model.IndexingUnit;


public interface IndexingUnitRepository extends ElasticsearchRepository<IndexingUnit, Long> {

}
