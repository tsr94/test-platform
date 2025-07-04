package com.test.testplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.testplatform.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	
}