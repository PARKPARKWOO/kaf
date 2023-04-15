package com.baeker.baeker.member;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.member.form.MemberLoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
}
