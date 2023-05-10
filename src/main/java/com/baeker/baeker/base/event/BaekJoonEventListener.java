package com.baeker.baeker.base.event;

import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.study.StudyService;
import com.baeker.baeker.studyRule.StudyRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BaekJoonEventListener {

    private final MemberService memberService;
    private final StudyService studyService;
    private final StudyRuleService studyRuleService;

    //-- Subscribe 목록 --//
    @EventListener
    public void listen(BaekJoonEvent event) {
        memberService.whenBaekJoonEventType(event.getMember(), event.getSolved());
        studyService.whenBaekJoonEventType(event.getMember().getId(), event.getSolved());
    }

    @EventListener
    public void listen(StudyEvent event) {
        studyRuleService.whenstudyEventType(event.getId());
    }
}



