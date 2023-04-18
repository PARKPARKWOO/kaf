package com.baeker.baeker.goal;

import com.baeker.baeker.member.Member;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.study.Study;
import com.baeker.baeker.studyRule.StudyRule;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer goal;

    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_mystudy")
    private MyStudy myStudy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_studyrule")
    private StudyRule studyRule;
}
