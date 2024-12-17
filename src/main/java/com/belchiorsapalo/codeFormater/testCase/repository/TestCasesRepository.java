package com.belchiorsapalo.codeFormater.testCase.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.belchiorsapalo.codeFormater.testCase.model.TestCase;

public interface TestCasesRepository extends JpaRepository<TestCase, UUID>{
    void deleteAllByProblemId(UUID id);
}
