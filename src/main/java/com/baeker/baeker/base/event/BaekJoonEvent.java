package com.baeker.baeker.base.event;

import com.baeker.baeker.base.entity.ScoreBase;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BaekJoonEvent extends ApplicationEvent {

    private final String eventCode;
    private final ScoreBase scoreBase;

    public BaekJoonEvent(Object source, ScoreBase score) {
        super(source);
        this.eventCode = "BaekJoon";
        this.scoreBase = score;
    }
}
