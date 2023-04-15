package com.baeker.baeker.studyRule;

import com.baeker.baeker.rule.Rule;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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

    @OneToOne(fetch = FetchType.LAZY)
    private Rule rule;

    // Goal 추가

    // study 추가

}
