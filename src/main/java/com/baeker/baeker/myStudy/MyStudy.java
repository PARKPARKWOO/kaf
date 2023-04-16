package com.baeker.baeker.myStudy;

import com.baeker.baeker.member.Member;
import com.baeker.baeker.study.Study;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class MyStudy {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StudyStatus studyStatus;

    @CreatedDate
    private LocalDateTime joinDate;


    @ManyToOne(fetch = LAZY, cascade = ALL)
    private Member member;

    @ManyToOne(fetch = LAZY)
    private Study study;
}
