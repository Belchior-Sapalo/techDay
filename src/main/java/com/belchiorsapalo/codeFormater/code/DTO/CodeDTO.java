 package com.belchiorsapalo.codeFormater.code.DTO;

import java.util.UUID;

public record CodeDTO(String language, String codeBody, String token, String inputs, UUID currentProblemId) {
}
