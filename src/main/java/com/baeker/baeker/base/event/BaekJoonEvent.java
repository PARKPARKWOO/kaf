package com.baeker.baeker.base.event;

import com.baeker.baeker.base.entity.Score;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BaekJoonEvent extends ApplicationEvent {

    private final String eventCode;
    private final Score score;
    private final Score oldScore;

    public BaekJoonEvent(Object source, Score score, Score oldScore) {
        super(source);
        this.eventCode = "BaekJoon";
        this.score = score;
        this.oldScore = oldScore;
    }
}
