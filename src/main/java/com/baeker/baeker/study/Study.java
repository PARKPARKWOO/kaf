package com.baeker.baeker.study;

import com.baeker.baeker.base.entity.ScoreBase;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.embed.BaekJoon;
import com.baeker.baeker.member.embed.Programmers;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.studyRule.StudyRule;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = PROTECTED)
public class Study extends ScoreBase {

    @Column(unique = true)
    private String name;
    private String about;
    private String leader;
    private Integer capacity;
    private Integer xp;

    @Embedded
    private BaekJoon baekJoon;
    @Embedded
    private Programmers programmers;

    @Builder.Default
    @OneToMany(mappedBy = "study")
    private List<MyStudy> myStudies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "study")
    private List<StudyRule> studyRules = new ArrayList<>();


    //-- create method --//
    public static Study createStudy(String name, String about, Integer capacity, Member member) {
        return builder()
                .name(name)
                .about(about)
                .leader(member.getNickName())
                .capacity(capacity)
                .xp(0)
                .build();
    }


    //-- business logic --//

    // 이름, 소개, 최대 인원 변경 //
    public Study modifyStudy(String name, String about, Integer capacity) {
        return this.toBuilder()
                .name(name)
                .about(about)
                .capacity(capacity)
                .modifyDate(LocalDateTime.now())
                .build();
    }

    // 리더 변경 //
    public Study modifyLeader(String leader) {
        return this.toBuilder()
                .leader(leader)
                .modifyDate(LocalDateTime.now())
                .build();
    }

    // 경험치 상승 //
    public void xpUp(Integer addXp) {
        this.xp += addXp;
    }


}
