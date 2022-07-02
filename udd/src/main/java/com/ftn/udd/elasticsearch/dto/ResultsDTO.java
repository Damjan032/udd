package com.ftn.udd.elasticsearch.dto;

import java.util.List;

import com.ftn.udd.elasticsearch.model.IndexingUnit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultsDTO {

	private List<IndexingUnit> content;
	private long totalElements;

}
