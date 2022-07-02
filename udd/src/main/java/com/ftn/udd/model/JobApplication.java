package com.ftn.udd.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "job_applications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplication {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column
	private String email;
	
	@Column(name = "education_degree")
	private String educationDegree;
	
	@Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;
    
    @Column(name = "cv_path")
    private String cvPath;
}


