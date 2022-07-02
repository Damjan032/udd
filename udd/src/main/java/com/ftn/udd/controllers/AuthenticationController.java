package com.ftn.udd.controllers;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.udd.dto.ResponseUserDTO;
import com.ftn.udd.dto.UserLoginDTO;
import com.ftn.udd.dto.UserTokenStateDTO;
import com.ftn.udd.model.User;
import com.ftn.udd.security.auth.TokenUtils;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:8090", maxAge = 3600, allowedHeaders = "*")
public class AuthenticationController {

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	public AuthenticationController() {

	}
	
	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody UserLoginDTO authenticationRequest) {
		Authentication authentication = null;
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getEmail(), authenticationRequest.getPassword()));
		} catch (Exception e) {
			return new ResponseEntity<>("Incorrect email or password.", HttpStatus.UNAUTHORIZED);
		}

		User user = (User) authentication.getPrincipal();
		
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenUtils.generateToken(user.getEmail(), "");
		int expiresIn = tokenUtils.getExpiredIn();
		return ResponseEntity.ok(new UserTokenStateDTO(jwt, (long) expiresIn));
	}

	@GetMapping(value = "/current-user")
	public ResponseEntity<?> currentUser() {
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
			return new ResponseEntity<>("Session expired.", HttpStatus.UNAUTHORIZED);
		}
		User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ResponseUserDTO responseUser = new ResponseUserDTO(current.getEmail());
		return new ResponseEntity<>(responseUser, HttpStatus.OK);
	}

}
