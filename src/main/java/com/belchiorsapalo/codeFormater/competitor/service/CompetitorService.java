package com.belchiorsapalo.codeFormater.competitor.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.belchiorsapalo.codeFormater.competitor.DTO.CompetitorLoginDTO;
import com.belchiorsapalo.codeFormater.competitor.DTO.CompetitorLoginResDTO;
import com.belchiorsapalo.codeFormater.competitor.DTO.CompetitorRegisterDTO;
import com.belchiorsapalo.codeFormater.competitor.DTO.GetCompetitorInfoDTO;
import com.belchiorsapalo.codeFormater.competitor.model.Competitor;
import com.belchiorsapalo.codeFormater.competitor.repository.CompetitorRepository;
import com.belchiorsapalo.codeFormater.exceptions.ResourceAlreadyExistsException;
import com.belchiorsapalo.codeFormater.infra.TokenService;

@Service
public class CompetitorService implements UserDetailsService {

    private final CompetitorRepository competitorRepository;
    private final TokenService tokenService;

    @Autowired
    public CompetitorService(CompetitorRepository competitorRepository, TokenService tokenService) {
        this.competitorRepository = competitorRepository;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String bi) throws UsernameNotFoundException {
        return competitorRepository.findUserByBi(bi);
    }

    public CompetitorLoginResDTO login(CompetitorLoginDTO competitorLoginDTO,
            AuthenticationManager authenticationManager) {
        UsernamePasswordAuthenticationToken usernamePasswordToken = new UsernamePasswordAuthenticationToken(
                competitorLoginDTO.bi(), competitorLoginDTO.password());
        var auth = authenticationManager.authenticate(usernamePasswordToken);
        String token = tokenService.generateToken((Competitor) auth.getPrincipal());
        Competitor competitor = (Competitor) competitorRepository.findUserByBi(competitorLoginDTO.bi());
        return new CompetitorLoginResDTO(token, competitor.getRole());
    }

    public Competitor register(CompetitorRegisterDTO competitorRegisterDTO) {
        var veryCompetitor = competitorRepository.findUserByBi(competitorRegisterDTO.bi());
        if (veryCompetitor != null)
            throw new ResourceAlreadyExistsException("O número de BI já existe");
        String encryptedPassword = new BCryptPasswordEncoder().encode(competitorRegisterDTO.password());

        if (competitorRegisterDTO.isAdmin() && competitorRepository.existsByRole("ADMIN"))
            throw new ResourceAlreadyExistsException("Já existe um administrador registrado!");

        Competitor newCompetitor = new Competitor(competitorRegisterDTO, encryptedPassword,
                competitorRegisterDTO.isAdmin() ? "ADMIN" : "USER");
        return competitorRepository.save(newCompetitor);
    }

    public GetCompetitorInfoDTO getCompetitorInfo(GetCompetitorInfoDTO tokenDTO) {
        String bi = tokenService.validateToken(tokenDTO.token());
        var foundedCompetitor = (Competitor) competitorRepository.findUserByBi(bi);
        return new GetCompetitorInfoDTO(null, foundedCompetitor.getName(), foundedCompetitor.getScore());
    }

    public List<Competitor> getChalangeResults() {
        return competitorRepository.findAll().stream()
                .filter(c -> c.getRole().equals("USER"))
                .sorted(Comparator.comparing(Competitor::getScore).reversed()
                        .thenComparing(Competitor::getName))
                .toList();
    }
}
