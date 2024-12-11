package com.belchiorsapalo.codeFormater.problem.DTO;

import java.util.List;

import com.belchiorsapalo.codeFormater.testCase.model.TestCase;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Min;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemRegisterDTO(
    String title, 
    String description,
    @Min(value = 1, message = "O valor da sequência deve ser maior ou igual que 1") 
    int sequence, 
    @Min(value = 1, message = "Os pontos pelo problema devem ser maior ou igual 1") 
    int points, 
    @Min(value = 10, message = "O tempo de duração deve ser maior ou igual a 10")
    int durationTime, 
    List<TestCase> testCases) {
}
