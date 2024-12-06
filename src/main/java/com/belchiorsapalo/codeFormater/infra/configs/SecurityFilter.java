package com.belchiorsapalo.codeFormater.infra.configs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.belchiorsapalo.codeFormater.infra.TokenService;
import com.belchiorsapalo.codeFormater.competitor.repository.CompetitorRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{

    private final TokenService tokenService;
    private final CompetitorRepository competitorRepository;

    @Autowired
    public SecurityFilter(TokenService tokenService, CompetitorRepository competitorRepository){
        this.tokenService = tokenService;
        this.competitorRepository = competitorRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException{
        var token = utilRecoverToken(request);
        if(token != null){
            String bi = tokenService.validateToken(token);
            UserDetails user = competitorRepository.findUserByBi(bi);
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String utilRecoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
    
}
