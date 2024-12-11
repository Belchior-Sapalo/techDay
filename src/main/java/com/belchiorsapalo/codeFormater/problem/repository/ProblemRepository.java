package com.belchiorsapalo.codeFormater.problem.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.belchiorsapalo.codeFormater.problem.model.Problem;

public interface ProblemRepository extends JpaRepository<Problem, UUID> {
    Optional<Problem> findProblemByTitleAndDescription(String title, String problem);

    Optional<Problem> findFirstBySequenceGreaterThanOrderBySequence(int sequence);

    Optional<Problem> findProblemBySequence(int sequence);


}
