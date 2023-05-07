package com.baeker.baeker.base.event;

import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.embed.BaekJoonDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BaekJoonEvent extends ApplicationEvent {

    private final Member member;
    private final BaekJoonDto solved;

    public BaekJoonEvent(Object source, Member member, BaekJoonDto solved) {
        super(source);
        this.member = member;
        this.solved = solved;
    }
}
