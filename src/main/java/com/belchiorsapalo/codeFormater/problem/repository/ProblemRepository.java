package com.belchiorsapalo.codeFormater.problem.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.belchiorsapalo.codeFormater.problem.model.Problem;

public interface ProblemRepository extends JpaRepository<Problem, UUID> {
    Optional<Problem> findProblemByTitleAndDescription(String title, String problem);

    Optional<Problem> findFirstBySequenceGreaterThanAndIsVisibleOrderBySequence(int sequence, boolean isVisible);

    Optional<Problem> findProblemByIsVisible(boolean isVisible);

    Optional<Problem> findProblemBySequence(int sequence);

}
