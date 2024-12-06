package com.belchiorsapalo.codeFormater.problem.DTO;

import java.util.UUID;

public record ProblemResponseDTO(UUID id, String title, String description, int sequence) {
    
}
