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
        return problemRepository.findAll().stream()
            .sorted((p1, p2) -> Integer.compare(p1.getSequence(), p2.getSequence())) 
            .toList()   
        ;
    }

    public Problem register(ProblemRegisterDTO pDto){
        Optional<Problem> verifyProblemByTitle = problemRepository.findProblemByTitleAndDescription(pDto.title(), pDto.description());
        if (verifyProblemByTitle.isPresent())
            throw new ResourceAlreadyExistsException("Essa pergunta já existe");
        Optional<Problem> verifyProblemBySequence = problemRepository.findProblemBySequence(pDto.sequence());
        if (verifyProblemBySequence.isPresent())
            throw new ResourceAlreadyExistsException("Já existe uma pergunta para a sequência fornecida");
        Problem newProblem = new Problem(pDto.title(), pDto.description(), pDto.sequence(), pDto.points(), pDto.durationTime(), pDto.testCases());
        pDto.testCases().stream().forEach(testeCase -> testeCase.setProblem(newProblem));
        return problemRepository.save(newProblem);
    }

    // public Problem getFirstProblem(){
    //     return problemRepository
    //         .findFirstBySequenceGreaterThanOrderBySequence( -1)
    //         .orElseThrow(() -> new ResourceNotFoundException("Problema não encontrado"));
    // }

    public Problem verifyProblems(int sequence){
        return problemRepository
            .findFirstBySequenceGreaterThanAndIsVisibleOrderBySequence(sequence, true)
            .orElse(null);
    }

    public Problem getNextProblem(){
        return problemRepository
            .findProblemByIsVisible(true)
            .orElse(null);
    }

    public void delete(UUID id){
        problemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Problema não encontrado"));
        problemRepository.deleteById(id);
    }

    public Problem update(UUID id){
        var verifyProblem = problemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Problema não encontrado"));
        List<Problem> problemList = problemRepository.findAll();

        problemList.forEach(problem -> {
            if (!problem.getId().toString().equals(id.toString())){
                problem.setVisible(false);
                problemRepository.save(problem);
            }else{
                verifyProblem.setVisible(true);
            }
        });
        return problemRepository.save(verifyProblem);
    }

    public void finish(){
        List<Problem> problemList = problemRepository.findAll();
        problemList.forEach(problem -> {
            problem.setVisible(false);
            problemRepository.save(problem);
        });
    }

    public boolean finished(){
        List<Problem> problemList = problemRepository.findAll();
        for (int i = 0; i < problemList.size(); i++) {
            if (problemList.get(i).isVisible())
                return false;
        }
        return true;
    }
}
