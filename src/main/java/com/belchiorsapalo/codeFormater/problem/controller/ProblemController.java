package com.belchiorsapalo.codeFormater.problem.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.belchiorsapalo.codeFormater.problem.DTO.ProblemRegisterDTO;
import com.belchiorsapalo.codeFormater.problem.DTO.ProblemResponseDTO;
import com.belchiorsapalo.codeFormater.problem.model.Problem;
import com.belchiorsapalo.codeFormater.problem.services.ProblemServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/problems")
public class ProblemController {
    private final ProblemServices problemServices;

    @Autowired
    public ProblemController(ProblemServices problemServices) {
        this.problemServices = problemServices;
    }

    @GetMapping
    public ResponseEntity<List<Problem>> getAll() {
        return ResponseEntity.ok().body(problemServices.getAll());
    }

    @GetMapping("/verify/{sequence}")
    public ResponseEntity<Problem> getCurrent(@PathVariable int sequence) {
        Problem problem = problemServices.verifyProblems(sequence);
        if (problem == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(problem);
    }

    @GetMapping("/next")
    public ResponseEntity<Problem> getNext() {
        Problem problem = problemServices.getNextProblem();
        if (problem == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(problem);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Problem> getCurrent(@PathVariable UUID id) {
        Problem problem = problemServices.getCurrentProblem(id);
        if (problem == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(problem);
    }

    @PostMapping("/register")
    public ResponseEntity<Problem> register(@Valid @RequestBody ProblemRegisterDTO pDto) {
        return ResponseEntity.ok().body(problemServices.register(pDto));
    }

    @PutMapping("/updateProblem/{id}")
    public ResponseEntity<Problem> updateProblem(@PathVariable UUID id, @RequestBody ProblemRegisterDTO pDto) {
        return ResponseEntity.ok().body(problemServices.updateProblem(pDto, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        problemServices.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/testCases/{id}")
    public ResponseEntity<Object> deleteTestCases(@PathVariable UUID id) {
        problemServices.deleteProblemTestCases(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Problem> update(@PathVariable UUID id) {
        return ResponseEntity.ok().body(problemServices.update(id));
    }

    @GetMapping("/{id}/time-left")
    public ResponseEntity<Object> getTimeLeft(@PathVariable UUID id) {
        return ResponseEntity.ok().body(problemServices.getTimeLeft(id));
    }

    @PutMapping("/finish")
    public ResponseEntity<Object> finish() {
        problemServices.finish();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/finished")
    public ResponseEntity<ProblemResponseDTO> finished() {
        return ResponseEntity.ok().body(problemServices.finished());
    }
}
