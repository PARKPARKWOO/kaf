package com.baeker.baeker.member;

import com.baeker.baeker.base.event.BaekJoonEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberEventListener {

    private final MemberService memberService;

    @EventListener
    @Transactional
    public void listen(BaekJoonEvent event) {
        memberService.whenBaekJoonEventType(event.getScoreBase(), event.getEventCode());
    }
}
