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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    //-- my profile --//
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String myProfile() {
        Member member = rq.getMember();
        log.info("내 프로필 요청 확인 member = {}", member.toString());
        return "member/profile";
    }

    //-- member profile --//
    @GetMapping("/member/{id}")
    public String profile(
            @PathVariable Long id,
            Model model
    ) {
        log.info("맴버 프로필 요청 확인 member id = {}", id);

        RsData<Member> memberRs = memberService.getMember(id);

        if (memberRs.isFail()) {
            log.info("getMember(id) 조회 실패 msa = {}", memberRs.getMsg());
            return rq.historyBack(memberRs.getMsg());
        }

        // 자기 자신의 프로필일 경우 리다이렉트
        Member member = memberRs.getData();
        if (member.getName().equals(rq.getMember().getName()))
            return "redirect:/member/profile";

        model.addAttribute("member", member);

        log.info("member 조회 성공 member name = {}", member.getName());
        return "member/member";
    }
}
