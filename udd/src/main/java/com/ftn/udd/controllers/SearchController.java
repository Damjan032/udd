package com.ftn.udd.controllers;

import java.util.List;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.udd.elasticsearch.dto.SearchParamDTO;
import com.ftn.udd.services.SearchService;

@Validated
@RestController
@RequestMapping(value = "/api")
public class SearchController {

	@Autowired
	private SearchService searchService;

	@RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> search(@RequestParam @PositiveOrZero int page, @RequestParam @Positive int size,
			@RequestParam(required = false) String searchValue, @RequestBody List<SearchParamDTO> searchParams) {

		if (searchValue != null && !searchValue.isEmpty()) {
			return new ResponseEntity<>(searchService.simpleQuery(searchValue, page, size), HttpStatus.OK);
		}

		return new ResponseEntity<>(searchService.advancedQuery(searchParams, page, size), HttpStatus.OK);
	}

	@RequestMapping(value = "/geo-search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> search(@RequestParam String place, @RequestParam int radius,
			@RequestParam @PositiveOrZero int page, @RequestParam @Positive int size) {

		try {
			return new ResponseEntity<>(searchService.geoSearch(place, radius, page, size), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
