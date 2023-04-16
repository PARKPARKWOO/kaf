package com.baeker.baeker.rule;

import com.baeker.baeker.studyRule.StudyRule;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Entity
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String about;

    private String provider;

//    private Integer xp; // Study에만 있으면 될거같음

//    @Builder.Default
//    @Convert(converter = RuleForm.class)
//    private Map<String, Object> attributes = new HashMap<>();

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    @OneToOne(fetch = FetchType.LAZY)
    private StudyRule studyRule;


    private String difficulty;
}
