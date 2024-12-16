package com.belchiorsapalo.codeFormater.problem.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.belchiorsapalo.codeFormater.problem.model.Problem;

import jakarta.persistence.LockModeType;

public interface ProblemRepository extends JpaRepository<Problem, UUID> {
    Optional<Problem> findProblemByTitleAndDescription(String title, String problem);

    Optional<Problem> findFirstBySequenceGreaterThanAndIsVisibleOrderBySequence(int sequence, boolean isVisible);

    Optional<Problem> findProblemByIsVisible(boolean isVisible);

    Optional<Problem> findProblemBySequence(int sequence);

    @Modifying
    @Query("UPDATE Problem p SET p.isVisible = :visible")
    void updateAllVisibility(@Param("visible") boolean visible);

    @Query("SELECT p FROM Problem p")
    @Lock(LockModeType.PESSIMISTIC_READ)
    List<Problem> findAllWithLock();

    boolean existsByIsVisibleTrue();

}
