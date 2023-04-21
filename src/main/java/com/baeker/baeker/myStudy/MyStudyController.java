package com.baeker.baeker.myStudy;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.myStudy.form.MyStudyInviteForm;
import com.baeker.baeker.study.Study;
import com.baeker.baeker.study.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/my_study")
@RequiredArgsConstructor
public class MyStudyController {

    private final MyStudyService myStudyService;
    private final MemberService memberService;
    private final StudyService studyService;
    private final Rq rq;

    //-- 가입 신청하기 --//

    //-- 스터디로 초대하기 --//
    @PostMapping("invite/{id}")
    @PreAuthorize("isAuthenticated()")
    public String invite(
            @PathVariable Long id,
            MyStudyInviteForm form
    ) {
        Member inviter = rq.getMember();
        log.info("스터디 초대 요청 확인 inviter id = {}", inviter.getId());

        RsData<Member> inviteeRs = memberService.getMember(id);
        if (inviteeRs.isFail()) {
            log.info("invitee 조회 실패 error = {}", inviteeRs.getMsg());
            return rq.historyBack(inviteeRs.getMsg());
        }

        RsData<Study> studyRs = studyService.getStudy(form.getStudyId());
        if (studyRs.isFail()) {
            log.info("study 조회 실패 error = {}", studyRs.getMsg());
            return rq.historyBack(studyRs.getMsg());
        }

        RsData<MyStudy> inviteRs
                = myStudyService.invite(inviter, inviteeRs.getData(), studyRs.getData(), form.getMsg());

        if (inviteeRs.isFail()) {
            log.info("초대 실패 error = {}", inviteRs.getMsg());
            return rq.historyBack(inviteRs.getMsg());
        }

        return rq.redirectWithMsg("/study/detail/" + studyRs.getData().getId(), inviteRs.getMsg());
    }
}
