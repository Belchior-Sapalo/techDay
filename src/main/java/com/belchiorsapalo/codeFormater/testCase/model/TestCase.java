package com.belchiorsapalo.codeFormater.testCase.model;

import java.io.Serializable;
import java.util.UUID;

import com.belchiorsapalo.codeFormater.problem.model.Problem;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_TESTCASE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestCase implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String inputs;

    @Column(nullable = false)
    private String expectedOutput;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Problem problem;
}
