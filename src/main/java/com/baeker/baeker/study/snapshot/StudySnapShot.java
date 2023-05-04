package com.baeker.baeker.study.snapshot;

import com.baeker.baeker.base.entity.ScoreBase;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.embed.BaekJoonDto;
import com.baeker.baeker.study.Study;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class StudySnapShot extends ScoreBase {

    private String studyName;
    private String dayOfWeek;

    @ManyToOne
    private Study study;

    //-- create score by dto --//
    public static StudySnapShot create(Study study, BaekJoonDto dto, String dayOfWeek) {
        StudySnapShot snapshot = StudySnapShot.builder()
                .study(study)
                .studyName(study.getName())
                .dayOfWeek(dayOfWeek)
                .bronze(dto.getBronze())
                .sliver(dto.getSliver())
                .gold(dto.getGold())
                .diamond(dto.getDiamond())
                .ruby(dto.getRuby())
                .platinum(dto.getPlatinum())
                .build();

        study.getSnapShotList().add(0, snapshot);
        return snapshot;
    }

    //-- create score by member --//
    public static StudySnapShot create(Study study, Member member, String dayOfWeek) {
        StudySnapShot snapshot = StudySnapShot.builder()
                .study(study)
                .studyName(study.getName())
                .dayOfWeek(dayOfWeek)
                .bronze(member.getBronze())
                .sliver(member.getSliver())
                .gold(member.getGold())
                .diamond(member.getDiamond())
                .ruby(member.getRuby())
                .platinum(member.getPlatinum())
                .build();

        study.getSnapShotList().add(0, snapshot);
        return snapshot;
    }


    //-- update score by dto --//
    public StudySnapShot update(BaekJoonDto dto) {
        return this.toBuilder()
                .bronze(this.getBronze() + dto.getBronze())
                .sliver(this.getSliver() + dto.getSliver())
                .gold(this.getGold() + dto.getGold())
                .diamond(this.getDiamond() + dto.getDiamond())
                .ruby(this.getRuby() + dto.getRuby())
                .platinum(this.getPlatinum() + dto.getPlatinum())
                .build();
    }
}
