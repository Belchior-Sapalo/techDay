package com.belchiorsapalo.codeFormater.code.services;

import com.belchiorsapalo.codeFormater.code.DTO.CodeDTO;
import com.belchiorsapalo.codeFormater.code.DTO.SubmitResponseDTO;
import com.belchiorsapalo.codeFormater.code.DTO.TestResponseDTO;
import com.belchiorsapalo.codeFormater.competitor.model.Competitor;
import com.belchiorsapalo.codeFormater.competitor.repository.CompetitorRepository;
import com.belchiorsapalo.codeFormater.exceptions.InvalidCodeException;
import com.belchiorsapalo.codeFormater.exceptions.InvalidLanguageException;
import com.belchiorsapalo.codeFormater.exceptions.ResourceNotFoundException;
import executor.Status;
import executor.Util;
import executor.Util.CodeType;
import com.belchiorsapalo.codeFormater.infra.TokenService;
import com.belchiorsapalo.codeFormater.problem.repository.ProblemRepository;
import tester.ConcurrentTester;
import tester.TestCase;
import tester.UserTestSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeService {

    private final TokenService tokenService;
    private final CompetitorRepository competitorRepository;
    private final ProblemRepository problemRepository;

    @Autowired
    public CodeService(TokenService tokenService, CompetitorRepository competitorRepository, ProblemRepository problemRepository) {
        this.tokenService = tokenService;
        this.competitorRepository = competitorRepository;
        this.problemRepository = problemRepository;
    }

    public SubmitResponseDTO processCode(CodeDTO code) throws InvalidCodeException, InterruptedException {
        ConcurrentTester tester = new ConcurrentTester();

        String bi = tokenService.validateToken(code.token());

        Competitor foundedCompetitor = (Competitor) competitorRepository.findUserByBi(bi);

        if (foundedCompetitor == null)
            throw new ResourceNotFoundException("Usuário não encontrado");

        var currentProblem = problemRepository.findById(code.currentProblemId()).orElseThrow(() -> new ResourceNotFoundException("Falha ao submeter problema"));

        List<String> inpuList = new ArrayList<>();
        List<String> outputList = new ArrayList<>();

        currentProblem.getTestCases().forEach(testCase -> {
            inpuList.add(testCase.getInputs());
            outputList.add(testCase.getExpectedOutput());
        });

        TestCase testCase = new TestCase(code.codeBody(), choseTypeCode(code.language()), inpuList, outputList);
        
        UserTestSummary result = tester.testSingleUser(testCase);

        System.out.println(result.getSummaryDetails());

        return new SubmitResponseDTO(currentProblem.getSequence(), true, result.getTotalPoints(), foundedCompetitor.getScore());
    }

    public TestResponseDTO testCode(CodeDTO code){
        String[] inputs = code.inputs().split(",");
        Status status = Util.execute(code.codeBody(), choseTypeCode(code.language()), inputs);
        return new TestResponseDTO(status.output);
    }

    private static Util.CodeType choseTypeCode(String language) {
        switch (language) {
            case "java":
                return CodeType.JAVA;
            case "c++":
                return CodeType.CPP;
            case "c":
                return CodeType.C;
            case "php":
                return CodeType.PHP;
            case "javascript":
                return CodeType.JS;
            case "python":
                return CodeType.PYTHON;
            default:
                throw new InvalidLanguageException("A linguagem é inválida");
        }
    }
}
