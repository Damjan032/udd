package com.ftn.udd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftn.udd.model.User;


@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

}
