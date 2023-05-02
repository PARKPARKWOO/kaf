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

    private String eventCode;
    private String nickName;

    @ManyToOne
    private Member member;

    //-- create method --//
    public static MemberSnapshot create(String eventCode, Member member) {
        MemberSnapshot snapshot = MemberSnapshot.builder()
                .eventCode(eventCode)
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
}
