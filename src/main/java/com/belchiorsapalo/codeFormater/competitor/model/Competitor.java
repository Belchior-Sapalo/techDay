package com.belchiorsapalo.codeFormater.competitor.model;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.belchiorsapalo.codeFormater.competitor.DTO.CompetitorRegisterDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_COMPETITOR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Competitor implements UserDetails {
    private static final long serialVersionUID = 1L;

    public Competitor(CompetitorRegisterDTO competitorRegisterDTO, String encryptedPassword){
        this.bi = competitorRegisterDTO.bi();
        this.name = competitorRegisterDTO.name();
        this.password = encryptedPassword;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String bi;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    private int score;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.bi;
    }
}
