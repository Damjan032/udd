package com.ftn.udd.controllers;

import com.ftn.udd.dto.CvDto;
import com.ftn.udd.repositories.IJobRepository;
import com.ftn.udd.services.GeoPointService;
import com.ftn.udd.utils.PdfUtils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:8090", maxAge = 3600, allowedHeaders = "*")
public class JobController {

	@Autowired
	private IJobRepository jobRepository;

	@Autowired
	private GeoPointService geoPointService;
	

	private static final Logger logger = LoggerFactory.getLogger(JobController.class.getName());

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllJobs() {

		try {
			return new ResponseEntity<>(jobRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/log-application", method = RequestMethod.GET)
	public ResponseEntity<?> logApplication(@RequestParam(required = true) double lat,
			@RequestParam(required = true) double lon) {
		String city;
		try {
			city = geoPointService.getCityByCoords(lat, lon);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>("Not existing.", HttpStatus.BAD_REQUEST);
		}
		if (city == null || city.equals("")) {
			return new ResponseEntity<>("Not existing.", HttpStatus.BAD_REQUEST);
		}
		logger.info("Someone accessed the form for job application. City=" + city);
		return new ResponseEntity<>("Successfully logged.", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/cv", method = RequestMethod.POST)
    public ResponseEntity<?> getCv(@RequestBody CvDto dto) {
        byte[] content;
		try {
			content = PdfUtils.readFile(dto.getPath());
			return new ResponseEntity<>(content, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
    }
}
