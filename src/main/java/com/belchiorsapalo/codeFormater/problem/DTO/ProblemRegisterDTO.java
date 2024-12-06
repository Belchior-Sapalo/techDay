package com.belchiorsapalo.codeFormater.problem.DTO;

import java.util.List;

import com.belchiorsapalo.codeFormater.testCase.model.TestCase;

public record ProblemRegisterDTO(String title, String description, int sequence, double points, List<TestCase> testCases) {
}
