package com.belchiorsapalo.codeFormater.infra;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.belchiorsapalo.codeFormater.competitor.model.Competitor;

@Service
public class TokenService {

    @Value("${api.token-secret}")
    private String secret;

    public String generateToken(Competitor competitor) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("codeFormaterApi")
                    .withSubject(competitor.getBi())
                    .withExpiresAt(utilSenerateExpirationDate())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Falha ao gerar token");
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String bi = JWT.require(algorithm)
                    .withIssuer("codeFormaterApi")
                    .build()
                    .verify(token)
                    .getSubject();
            return bi;
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Falha ao verificar token");
        }
    }

    private Instant utilSenerateExpirationDate() {
        return LocalDateTime.now().plusHours(10).toInstant(ZoneOffset.of("-03:00"));
    }
}
