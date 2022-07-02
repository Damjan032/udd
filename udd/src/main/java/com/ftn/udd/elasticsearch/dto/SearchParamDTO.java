package com.ftn.udd.elasticsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ftn.udd.elasticsearch.enumeration.SearchType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchParamDTO {

	@NotBlank
	private String attributeName;

	@NotBlank
	private String searchValue;
	
	private String searchValue2;

	@NotNull
	private Boolean phraseQuery;

	private SearchType type;
}
