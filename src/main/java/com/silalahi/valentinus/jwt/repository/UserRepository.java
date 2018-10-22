package com.silalahi.valentinus.jwt.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.silalahi.valentinus.jwt.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	boolean existByUsername(String username);

	User findByUsername(String username);

	@Transactional
	void deleteByUsername(String username);

}
