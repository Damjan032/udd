package com.ftn.udd.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.udd.dto.JobApplicationDTO;
import com.ftn.udd.services.SearchService;

@RestController
@RequestMapping(value = "/api/elastic")
@CrossOrigin(origins = "http://localhost:8090", maxAge = 3600, allowedHeaders = "*")
public class ElasticSearchController {

	@Autowired
	private SearchService searchService;

	private static final Logger logger = LoggerFactory.getLogger(ElasticSearchController.class.getName());

	@PostMapping(value = "/upload-index", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadAndIndex(@ModelAttribute JobApplicationDTO jobApplication) {
		try {
			return new ResponseEntity<>(searchService.index(jobApplication), HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/log")
	public ResponseEntity<?> log() {
		logger.info("Evo me. City=Novi Sad");
		return new ResponseEntity<>("log", HttpStatus.OK);
	}

}
