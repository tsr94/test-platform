package com.test.testplatform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.testplatform.model.TestConfig;

public interface TestConfigRepository extends JpaRepository<TestConfig, Long> {
	 Optional<TestConfig> findBySlug(String slug);
}

