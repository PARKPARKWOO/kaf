package com.baeker.baeker.base.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BaekJoonEvent extends ApplicationEvent {

    private final String eventCode;
    private final String baekJoonName;
    private final int score;

    public BaekJoonEvent(Object source, String eventCode, String baekJoonName, int score) {
        super(source);
        this.eventCode = eventCode;
        this.baekJoonName = baekJoonName;
        this.score = score;
    }

}
