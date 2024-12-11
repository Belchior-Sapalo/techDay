package com.belchiorsapalo.codeFormater.competitor.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CompetitorRegisterDTO(
    @NotNull(message = "O nome não deve ser nulo") String name, 
    @NotNull @Size(min = 14, max = 14, message = "O número do BI deve possior 14 caracteres") @Pattern(regexp = "^\\d{9}[A-Z]{2}\\d{3}$", message = "O número do BI deve ser válido") String bi, 
    @NotNull @Size(min = 6, message = "A senha deve ter, no mínimo, 6 caracteres") String password,
    boolean isAdmin
) {
    
}
