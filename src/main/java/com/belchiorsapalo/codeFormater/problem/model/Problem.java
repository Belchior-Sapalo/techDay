package com.belchiorsapalo.codeFormater.problem.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.belchiorsapalo.codeFormater.competitorResInfo.model.CompetitorResInfo;
import com.belchiorsapalo.codeFormater.testCase.model.TestCase;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_PROBLEM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Problem implements Serializable{
    private static final long serialVersionUID = 1L;

    public Problem(String title, String description, int sequence, int points, int durationTime, List<TestCase> testCases){
        this.title = title;
        this.description = description;
        this.sequence = sequence;
        this.testCases = testCases;
        this.points = points;
        this.durationTime = durationTime;
        this.isVisible = false;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, unique = true)
    private int sequence;

    @Column(nullable = false)
    private int points;

    @Column(nullable = false)
    private boolean isVisible;

    @Column(nullable = false)
    private int durationTime;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    List<TestCase> testCases;

    @OneToMany(mappedBy = "problem")
    List<CompetitorResInfo> cInfos;
}
