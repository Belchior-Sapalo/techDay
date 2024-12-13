package com.belchiorsapalo.codeFormater.code.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.belchiorsapalo.codeFormater.code.DTO.CodeDTO;
import com.belchiorsapalo.codeFormater.code.DTO.SubmitResponseDTO;
import com.belchiorsapalo.codeFormater.code.DTO.TestResponseDTO;
import com.belchiorsapalo.codeFormater.competitor.model.Competitor;
import com.belchiorsapalo.codeFormater.competitor.repository.CompetitorRepository;
import com.belchiorsapalo.codeFormater.competitorResInfo.model.CompetitorResInfo;
import com.belchiorsapalo.codeFormater.competitorResInfo.repository.CompetitorResInfoRepository;
import com.belchiorsapalo.codeFormater.exceptions.InvalidCodeException;
import com.belchiorsapalo.codeFormater.exceptions.InvalidLanguageException;
import com.belchiorsapalo.codeFormater.exceptions.ResourceAlreadyExistsException;
import com.belchiorsapalo.codeFormater.exceptions.ResourceNotFoundException;
import com.belchiorsapalo.codeFormater.infra.TokenService;
import com.belchiorsapalo.codeFormater.problem.model.Problem;
import com.belchiorsapalo.codeFormater.problem.repository.ProblemRepository;

import executor.Status;
import executor.Util;
import executor.Util.CodeType;
import jakarta.transaction.Transactional;
import tester.ConcurrentTester;
import tester.TestCase;
import tester.UserTestSummary;

@Service
public class CodeService {

    private final TokenService tokenService;
    private final CompetitorRepository competitorRepository;
    private final ProblemRepository problemRepository;
    private final CompetitorResInfoRepository cResInfoRepository;

    @Autowired
    public CodeService(TokenService tokenService, CompetitorRepository competitorRepository,
            ProblemRepository problemRepository, CompetitorResInfoRepository cResInfoRepository) {
        this.tokenService = tokenService;
        this.competitorRepository = competitorRepository;
        this.problemRepository = problemRepository;
        this.cResInfoRepository = cResInfoRepository;
    }

    @Transactional
    public SubmitResponseDTO processCode(CodeDTO code) throws InvalidCodeException, InterruptedException {
        ConcurrentTester tester = new ConcurrentTester();

        String bi = tokenService.validateToken(code.token());

        Competitor foundedCompetitor = (Competitor) competitorRepository.findUserByBi(bi);

        if (foundedCompetitor == null)
            throw new ResourceNotFoundException("Usuário não encontrado");

        var currentProblem = problemRepository.findById(code.currentProblemId())
                .orElseThrow(() -> new ResourceNotFoundException("Falha ao submeter problema"));

        List<String> inpuList = new ArrayList<>();
        List<String> outputList = new ArrayList<>();

        currentProblem.getTestCases().forEach(testCase -> {
            inpuList.add(testCase.getInputs());
            outputList.add(testCase.getExpectedOutput());
        });

        TestCase testCase = new TestCase(code.codeBody(), choseTypeCode(code.language()), inpuList, outputList);

        UserTestSummary result = tester.testSingleUser(testCase);

        boolean verifyResInfo = cResInfoRepository.existsByCompetitorBiAndProblemId(bi, currentProblem.getId());

            if (verifyResInfo)
                throw new ResourceAlreadyExistsException("Já mandou a sua resolução para esse problema!");
        int bonusPoints = 0;
        if (result.getPassed()) {
            
            CompetitorResInfo cResInfo = new CompetitorResInfo(System.currentTimeMillis(), bi);

            currentProblem.getCInfos().add(cResInfo);
            cResInfo.setProblem(currentProblem);
            problemRepository.save(currentProblem);
            bonusPoints = assignPoints(cResInfo, foundedCompetitor, currentProblem);
        }

        return new SubmitResponseDTO(currentProblem.getSequence(), result.getPassed(), currentProblem.getPoints(), bonusPoints,
                foundedCompetitor.getScore());
    }

    @Transactional
    public int assignPoints(CompetitorResInfo cResInfo, Competitor competitor, Problem currentProblem) {
        int basePoints = currentProblem.getPoints();
        Pageable topThree = PageRequest.of(0, 3);
            List<CompetitorResInfo> firstThreeSolutions = cResInfoRepository
                    .findFirstThreeCompetitorResInfo(currentProblem.getId(), topThree);

        // Verificar se a solução atual está entre as três primeiras
        if (firstThreeSolutions.size() < 3) {
            int bonusPoints = 3 - (firstThreeSolutions.size()); // 5 para 1º, 4 para 2º, 3 para 3º
            competitor.updatePoints(basePoints + bonusPoints);
        }else{
            competitor.updatePoints(basePoints);
        }
        cResInfoRepository.save(cResInfo);
        competitorRepository.save(competitor);
        return 3 - firstThreeSolutions.size();
    }

    public TestResponseDTO testCode(CodeDTO code) throws IOException {
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
