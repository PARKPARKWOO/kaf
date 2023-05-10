package com.baeker.baeker.base.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * param
 * 스터디룰 id
 */
@Getter
public class StudyEvent extends ApplicationEvent {
    private final Long id;


    public StudyEvent(Object source, Long id) {
        super(source);
        this.id = id;
    }
}
