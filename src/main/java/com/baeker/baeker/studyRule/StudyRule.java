package com.baeker.baeker.studyRule;

import com.baeker.baeker.rule.Rule;
import com.baeker.baeker.study.Study;
import com.baeker.baeker.studyRule.solvedApi.SolvedApiManager;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class StudyRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String about;

    private Double rate;  // double 랭킹?

    @CreatedDate
    private LocalDateTime selectDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id")
    private Rule rule;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_study")
    private Study study;
}
