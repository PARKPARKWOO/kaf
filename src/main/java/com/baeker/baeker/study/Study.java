package com.baeker.baeker.study;

import com.baeker.baeker.member.Member;
import com.baeker.baeker.myStudy.MyStudy;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Study {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
    private String about;
    private String leader;
    private Integer capacity;
    private Integer xp;

    @CreatedDate
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @Builder.Default
    @OneToMany(mappedBy = "study")
    private List<MyStudy> myStudies = new ArrayList<>();


    //-- create method --//
    public static MyStudy createStudy(String name, String about, Integer capacity, Member member) {
        Study study = builder()
                .name(name)
                .about(about)
                .leader(member.getName())
                .capacity(capacity)
                .xp(0)
                .build();

        return MyStudy.createNewStudy(member, study);
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
