package com.ftn.udd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftn.udd.model.JobApplication;

@Repository
public interface IJobApplicationRepository extends JpaRepository<JobApplication, Long> {

}
