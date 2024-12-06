package com.belchiorsapalo.codeFormater.problem.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.belchiorsapalo.codeFormater.exceptions.ResourceAlreadyExistsException;
import com.belchiorsapalo.codeFormater.exceptions.ResourceNotFoundException;
import com.belchiorsapalo.codeFormater.problem.DTO.ProblemRegisterDTO;
import com.belchiorsapalo.codeFormater.problem.model.Problem;
import com.belchiorsapalo.codeFormater.problem.repository.ProblemRepository;

@Service
public class ProblemServices {
    private final ProblemRepository problemRepository;

    @Autowired
    public ProblemServices(ProblemRepository problemRepository){
        this.problemRepository = problemRepository;
    }

    public List<Problem> getAll(){
        return problemRepository.findAll();
    }

    public Problem register(ProblemRegisterDTO pDto){
        Optional<Problem> verifyProblem = problemRepository.findProblemByTitleAndDescription(pDto.title(), pDto.description());
        if (verifyProblem.isPresent())
            throw new ResourceAlreadyExistsException("Essa pergunta já existe");
        Problem newProblem = new Problem(pDto.title(), pDto.description(), pDto.sequence(), pDto.points(), pDto.testCases());
        pDto.testCases().stream().forEach(testeCase -> testeCase.setProblem(newProblem));
        return problemRepository.save(newProblem);
    }

    public Problem getFirstProblem(){
        return problemRepository
            .findFirstBySequenceGreaterThanOrderBySequence(-1)
            .orElseThrow(() -> new ResourceNotFoundException("Problema não encontrado"));
    }

    public Problem getCurrent(int sequence){
        return problemRepository
            .findProblemBySequence(sequence)
            .orElse(null);
    }

    public Problem getNextProblem(int sequence){
        return problemRepository
            .findFirstBySequenceGreaterThanOrderBySequence(sequence)
            .orElse(null);
    }

    public void delete(UUID id){
        problemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Problema não encontrado"));
        problemRepository.deleteById(id);
    }

    public Problem update(UUID id, ProblemRegisterDTO pDto){
        var verifyProblem = problemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Problema não encontrado"));
        if (!pDto.title().equals(verifyProblem.getTitle()))
            verifyProblem.setTitle(pDto.title());
        if (!pDto.description().equals(verifyProblem.getDescription()))
            verifyProblem.setDescription(pDto.description());
        return problemRepository.save(verifyProblem);
    }
}
