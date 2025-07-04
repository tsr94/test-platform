package com.test.testplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.testplatform.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
