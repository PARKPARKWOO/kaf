package com.baeker.baeker.base.event;

import com.baeker.baeker.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BaekJoonEventListener {

    private final MemberService memberService;

    //-- Subscribe 목록 --//
    @EventListener
    @Transactional
    public void listen(BaekJoonEvent event) {
        memberService.whenBaekJoonEventType(event.getBaekJoonName(), event.getScore(), event.getEventCode());
    }
}



