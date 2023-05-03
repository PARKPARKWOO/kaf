package com.baeker.baeker.study.snapshot;

import com.baeker.baeker.base.entity.ScoreBase;
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

    @ManyToOne
    private Study study;

    //-- create score --//
    public static StudySnapShot create(Study study) {
        StudySnapShot snapshot = StudySnapShot.builder()
                .study(study)
                .studyName(study.getName())
                .bronze(study.getBronze())
                .sliver(study.getSliver())
                .gold(study.getGold())
                .diamond(study.getDiamond())
                .ruby(study.getRuby())
                .platinum(study.getPlatinum())
                .build();

        study.getSnapShotList().add(snapshot);

        return snapshot;
    }

    //-- update score --//
    public StudySnapShot update(int score, String eventCode) {
        StudySnapShot snapshot;

        switch (eventCode) {
            case "bronze" -> snapshot = addBronze(score);
            case "sliver" -> snapshot = addSliver(score);
            case "gold" -> snapshot = addGold(score);
            case "diamond" -> snapshot = addDiamond(score);
            case "ruby" -> snapshot = addRuby(score);
            default -> snapshot = addPlatinum(score);
        }
        return snapshot;
    }
    private StudySnapShot addBronze(int score) {return this.toBuilder().bronze(score).build();}
    private StudySnapShot addSliver(int score) {return this.toBuilder().sliver(score).build();}
    private StudySnapShot addGold(int score) {return this.toBuilder().gold(score).build();}
    private StudySnapShot addDiamond(int score) {return this.toBuilder().diamond(score).build();}
    private StudySnapShot addRuby(int score) {return this.toBuilder().ruby(score).build();}
    private StudySnapShot addPlatinum(int score) {return this.toBuilder().platinum(score).build();}
}
