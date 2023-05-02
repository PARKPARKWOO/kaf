package com.baeker.baeker.member.snapshot;

import com.baeker.baeker.base.entity.ScoreBase;
import com.baeker.baeker.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MemberSnapshot extends ScoreBase {

    private String nickName;

    @ManyToOne
    private Member member;

    //-- create score --//
    public static MemberSnapshot create(Member member) {
        MemberSnapshot snapshot = MemberSnapshot.builder()
                .member(member)
                .nickName(member.getNickName())
                .bronze(member.getBronze())
                .sliver(member.getSliver())
                .gold(member.getGold())
                .diamond(member.getDiamond())
                .ruby(member.getRuby())
                .platinum(member.getPlatinum())
                .build();

        member.getSnapshotList().add(snapshot);

        return snapshot;
    }

    //-- update score --//
    public MemberSnapshot update(int score, String eventCode) {
        MemberSnapshot snapshot;

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
    private MemberSnapshot addBronze(int score) {return this.toBuilder().bronze(score).build();}
    private MemberSnapshot addSliver(int score) {return this.toBuilder().sliver(score).build();}
    private MemberSnapshot addGold(int score) {return this.toBuilder().gold(score).build();}
    private MemberSnapshot addDiamond(int score) {return this.toBuilder().diamond(score).build();}
    private MemberSnapshot addRuby(int score) {return this.toBuilder().ruby(score).build();}
    private MemberSnapshot addPlatinum(int score) {return this.toBuilder().platinum(score).build();}
}
