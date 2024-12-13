package com.belchiorsapalo.codeFormater.competitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.belchiorsapalo.codeFormater.competitor.DTO.CompetitorLoginDTO;
import com.belchiorsapalo.codeFormater.competitor.DTO.CompetitorLoginResDTO;
import com.belchiorsapalo.codeFormater.competitor.DTO.CompetitorRegisterDTO;
import com.belchiorsapalo.codeFormater.competitor.DTO.GetCompetitorInfoDTO;
import com.belchiorsapalo.codeFormater.competitor.model.Competitor;
import com.belchiorsapalo.codeFormater.competitor.service.CompetitorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/competitor")
public class CompetitorController {
    private final CompetitorService competitorService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public CompetitorController(CompetitorService competitorService, AuthenticationManager authenticationManager){
        this.competitorService = competitorService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<CompetitorLoginResDTO> login(@RequestBody CompetitorLoginDTO competitorCredentials){
        return ResponseEntity.ok().body(competitorService.login(competitorCredentials, authenticationManager));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Competitor> register(@Valid @RequestBody CompetitorRegisterDTO competitorRegisterDTO){
        return ResponseEntity.ok().body(competitorService.register(competitorRegisterDTO));
    }

    @PostMapping("/info")
    public ResponseEntity<GetCompetitorInfoDTO> getCompetitor(@RequestBody GetCompetitorInfoDTO tokenDTO){
        return ResponseEntity.ok().body(competitorService.getCompetitorInfo(tokenDTO));
    }

    @GetMapping("/results")
    public ResponseEntity<List<Competitor>> getChalangeResults(){
        return ResponseEntity.ok().body(competitorService.getChalangeResults());
    }
}
