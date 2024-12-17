package com.belchiorsapalo.codeFormater.problem.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.belchiorsapalo.codeFormater.exceptions.ResourceAlreadyExistsException;
import com.belchiorsapalo.codeFormater.exceptions.ResourceNotFoundException;
import com.belchiorsapalo.codeFormater.problem.DTO.ProblemRegisterDTO;
import com.belchiorsapalo.codeFormater.problem.DTO.ProblemResponseDTO;
import com.belchiorsapalo.codeFormater.problem.model.Problem;
import com.belchiorsapalo.codeFormater.problem.repository.ProblemRepository;
import com.belchiorsapalo.codeFormater.testCase.repository.TestCasesRepository;

import jakarta.transaction.Transactional;

@Service
public class ProblemServices {
    private final ProblemRepository problemRepository;
    private final TestCasesRepository testCasesRepository;

    @Autowired
    public ProblemServices(ProblemRepository problemRepository, TestCasesRepository testCasesRepository) {
        this.problemRepository = problemRepository;
        this.testCasesRepository = testCasesRepository;
    }

    public List<Problem> getAll() {
        return problemRepository.findAll().stream()
                .sorted((p1, p2) -> Integer.compare(p1.getSequence(), p2.getSequence()))
                .toList();
    }

    public Problem register(ProblemRegisterDTO pDto) {
        Optional<Problem> verifyProblemByTitle = problemRepository.findProblemByTitleAndDescription(pDto.title(),
                pDto.description());
        if (verifyProblemByTitle.isPresent())
            throw new ResourceAlreadyExistsException("Essa pergunta já existe");
        Optional<Problem> verifyProblemBySequence = problemRepository.findProblemBySequence(pDto.sequence());
        if (verifyProblemBySequence.isPresent())
            throw new ResourceAlreadyExistsException("Já existe uma pergunta para a sequência fornecida");
        Problem newProblem = new Problem(pDto.title(), pDto.description(), pDto.sequence(), pDto.points(),
                pDto.durationTime(), pDto.testCases());
        pDto.testCases().stream().forEach(testeCase -> testeCase.setProblem(newProblem));
        return problemRepository.save(newProblem);
    }

    public Problem updateProblem(ProblemRegisterDTO pDto, UUID id) {
        Problem problemToUpdate = problemRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Problema não encontrado"));
        problemToUpdate.setTitle(pDto.title());
        problemToUpdate.setDescription(pDto.description());
        problemToUpdate.setSequence(pDto.sequence());
        problemToUpdate.setPoints(pDto.points());
        problemToUpdate.setDurationTime(pDto.durationTime());
        problemToUpdate.setTestCases(pDto.testCases());
        pDto.testCases().stream().forEach(testeCase -> testeCase.setProblem(problemToUpdate));
        return problemRepository.save(problemToUpdate);
    }

    public Problem getCurrentProblem(UUID id) {
        return problemRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Problema não encontrado"));
    }

    public Problem verifyProblems(int sequence) {
        return problemRepository
                .findFirstBySequenceGreaterThanAndIsVisibleOrderBySequence(sequence, true)
                .orElse(null);
    }

    public Problem getNextProblem() {
        return problemRepository
                .findProblemByIsVisible(true)
                .orElse(null);
    }

    @Transactional
    public void delete(UUID id) {
        problemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Problema não encontrado"));
        utilDeleteProblmeTestCases(id);
        problemRepository.deleteById(id);
    }

    private void utilDeleteProblmeTestCases(UUID id){
        testCasesRepository.deleteAllByProblemId(id);
    }

    @Transactional
    public Problem update(UUID id) {
        problemRepository.updateAllVisibility(false);

        var verifyProblem = problemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Problema não encontrado"));

        verifyProblem.setVisible(true);

        if (verifyProblem.getStartTime() == null) {
            verifyProblem.setStartTime(LocalDateTime.now());
        }

        return problemRepository.save(verifyProblem);
    }

    public Object getTimeLeft(UUID id) {
        Optional<Problem> optionalProblem = problemRepository.findById(id);
        if (optionalProblem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Problema não encontrado");
        }

        Problem problem = optionalProblem.get();
        LocalDateTime deadline = problem.getStartTime().plusMinutes(problem.getDurationTime());
        long timeLeftInSeconds = Duration.between(LocalDateTime.now(), deadline).getSeconds();

        if ((timeLeftInSeconds + 5) <= 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("O tempo para resolver este problema expirou.");
        }

        return ResponseEntity.ok(Map.of("timeLeftInSeconds", (timeLeftInSeconds + 5)));
    }

    public void finish() {
        List<Problem> problemList = problemRepository.findAll();
        problemList.forEach(problem -> {
            problem.setVisible(false);
            problem.setStartTime(null);
            problemRepository.save(problem);
        });
    }

    @Transactional
    public ProblemResponseDTO finished() {
        boolean anyVisible = problemRepository.existsByIsVisibleTrue();
        if (anyVisible) {
            return new ProblemResponseDTO(null, null, null, 0, false);
        }
        return new ProblemResponseDTO(null, null, null, 0, true);
    }
}
