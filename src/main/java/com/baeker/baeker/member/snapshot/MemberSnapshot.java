package com.baeker.baeker.member.snapshot;

import com.baeker.baeker.base.entity.ScoreBase;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.embed.BaekJoonDto;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MemberSnapshot extends ScoreBase {

    private String baekJoonName;

    @ManyToOne
    private Member member;

    //-- create score --//
    public static MemberSnapshot create(Member member, BaekJoonDto dto) {
        MemberSnapshot snapshot = MemberSnapshot.builder()
                .member(member)
                .baekJoonName(member.getBaekJoonName())
                .bronze(dto.getBronze())
                .sliver(dto.getSliver())
                .gold(dto.getGold())
                .diamond(dto.getDiamond())
                .ruby(dto.getRuby())
                .platinum(dto.getPlatinum())
                .build();

        member.getSnapshotList().add(0, snapshot);
        return snapshot;
    }

    //-- update score --//
    public MemberSnapshot update(BaekJoonDto dto) {
        return this.toBuilder()
                .bronze(this.getBronze() + dto.getBronze())
                .sliver(this.getSliver() + dto.getSliver())
                .gold(this.getGold() + dto.getGold())
                .diamond(this.getDiamond() + dto.getDiamond())
                .ruby(this.getRuby() + dto.getRuby())
                .platinum(this.getPlatinum() + dto.getPlatinum())
                .build();
    }



    //-- initdb 용 create --//
    public static MemberSnapshot initDbCreate(Member member, BaekJoonDto dto, LocalDateTime createDate) {
        MemberSnapshot snapshot = MemberSnapshot.builder()
                .member(member)
                .baekJoonName(member.getBaekJoonName())
                .bronze(dto.getBronze())
                .sliver(dto.getSliver())
                .gold(dto.getGold())
                .diamond(dto.getDiamond())
                .ruby(dto.getRuby())
                .platinum(dto.getPlatinum())
                .createDate(createDate)
                .build();

        member.getSnapshotList().add(0, snapshot);
        return snapshot;
    }
}
