package com.belchiorsapalo.codeFormater.code.DTO;

public record SubmitResponseDTO(int exerciseNumber, boolean isCorrect, int score, int bonus, int totalScore) {
}
