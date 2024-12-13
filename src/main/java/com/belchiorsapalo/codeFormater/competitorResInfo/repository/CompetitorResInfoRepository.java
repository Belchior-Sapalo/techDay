package com.belchiorsapalo.codeFormater.competitorResInfo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.belchiorsapalo.codeFormater.competitorResInfo.model.CompetitorResInfo;

import jakarta.persistence.LockModeType;

public interface CompetitorResInfoRepository extends JpaRepository<CompetitorResInfo, UUID> {
    boolean existsByCompetitorBiAndProblemId(String bi, UUID id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CompetitorResInfo c WHERE c.problem.id = :problemId ORDER BY c.submissionTime ASC")
    List<CompetitorResInfo> findFirstThreeCompetitorResInfo(UUID problemId, Pageable pageable);

}
