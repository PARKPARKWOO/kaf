package com.baeker.baeker.member;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.form.MemberJoinForm;
import com.baeker.baeker.member.form.MemberLoginForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final Rq rq;


    //-- login --//
    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String login(MemberLoginForm form) {

        log.info("login form 요청 확인");
        return "member/login";
    }

    //-- join form -- //
    @GetMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String joinForm(MemberJoinForm form) {

        log.info("회원가입 폼 요청 확인");
        return "member/join";
    }

    //-- join 처리 --//
    @PostMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String join(@Valid MemberJoinForm form) {
        log.info("회원가입 처리 요청 확인 username = {}, name = {}", form.getUsername(), form.getName());

        RsData<Member> memberRs = memberService.join(form);

        if (memberRs.isFail()) {
            log.info("회원가입 실패 error = {}", memberRs.getMsg());
            return rq.historyBack(memberRs);
        }

        log.info("회원가입 성공 username = {}", memberRs.getData().getUsername());
        return rq.redirectWithMsg("/member/login", memberRs.getMsg());
    }
}
