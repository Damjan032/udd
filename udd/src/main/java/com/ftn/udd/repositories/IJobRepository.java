package com.ftn.udd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftn.udd.model.Job;

@Repository
public interface IJobRepository extends JpaRepository<Job, Long> {

}
