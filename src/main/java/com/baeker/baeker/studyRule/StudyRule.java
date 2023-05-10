package com.baeker.baeker.studyRule;

import com.baeker.baeker.base.entity.BaseEntity;
import com.baeker.baeker.rule.Rule;
import com.baeker.baeker.study.Study;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder(toBuilder = true)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class StudyRule extends BaseEntity {

    private String name;

    private String about;

    private Double rate;  // double 랭킹?

    @Enumerated(EnumType.STRING)
    private Mission mission;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "rule_id")
    private Rule rule;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    protected void setMission(boolean mission) {
        if (mission) {
            this.mission = Mission.COMPLETE;
        } else {
            this.mission = Mission.FAIL;
        }
    }
}
