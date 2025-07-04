package com.test.testplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.testplatform.model.Result;
import com.test.testplatform.model.User;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByUser(User user);
}
