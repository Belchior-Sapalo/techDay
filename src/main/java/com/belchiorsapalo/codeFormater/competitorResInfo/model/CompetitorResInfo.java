package com.belchiorsapalo.codeFormater.competitorResInfo.model;

import java.io.Serializable;
import java.util.UUID;

import com.belchiorsapalo.codeFormater.problem.model.Problem;

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
@Table(name = "TB_COMPETITOR_INFO_RES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompetitorResInfo implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private long submissionTime;

    @Column(nullable = false, unique = true)
    private String competitorBi;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    public CompetitorResInfo(long submissionTime, String bi){
        this.submissionTime = submissionTime;
        this.competitorBi = bi;
    }
}
