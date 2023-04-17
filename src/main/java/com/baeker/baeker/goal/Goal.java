package com.baeker.baeker.goal;

import com.baeker.baeker.member.Member;
import com.baeker.baeker.studyRule.StudyRule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer goal;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private boolean status;

}
