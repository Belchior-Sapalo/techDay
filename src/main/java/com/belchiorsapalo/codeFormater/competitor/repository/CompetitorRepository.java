package com.belchiorsapalo.codeFormater.competitor.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.belchiorsapalo.codeFormater.competitor.model.Competitor;

public interface CompetitorRepository extends JpaRepository<Competitor, UUID>{
    UserDetails findUserByBi(String bi); 
    boolean existsByRole(String role);
}
