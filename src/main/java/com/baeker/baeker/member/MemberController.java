package com.baeker.baeker.member;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.form.MemberJoinForm;
import com.baeker.baeker.member.form.MemberLoginForm;
import com.baeker.baeker.member.form.MemberModifyForm;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.myStudy.MyStudyService;
import com.baeker.baeker.myStudy.form.MyStudyInviteForm;
import com.baeker.baeker.study.Study;
import com.baeker.baeker.study.StudyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String joinForm(MemberJoinForm form, Model model) {
        log.info("회원가입 폼 요청 확인");

        List<Integer> random = memberService.random();

        model.addAttribute("random", random);
        log.info("회원가입 폼 응답 완료");
        return "member/join";
    }

    //-- join 처리 --//
    @PostMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String join(@Valid MemberJoinForm form) {
        log.info("회원가입 처리 요청 확인 username = {}, name = {}", form.getUsername(), form.getNickName());

        RsData<Member> memberRs = memberService.join(form);

        if (memberRs.isFail()) {
            log.info("회원가입 실패 error = {}", memberRs.getMsg());
            return rq.historyBack(memberRs);
        }

        log.info("회원가입 성공 username = {}", memberRs.getData().getUsername());
        return rq.redirectWithMsg("/member/login", memberRs.getMsg());
    }

    //-- my profile --//
    @GetMapping("/profile/{list}")
    @PreAuthorize("isAuthenticated()")
    public String myProfile(
            @PathVariable String list,
            Model model
    ) {
        Member member = rq.getMember();
        log.info("내 프로필 요청 확인 member = {}", member.toString());

        model.addAttribute("list", list);
        log.info("내 프로필 응답 완료");
        return "member/profile";
    }


    //-- member profile --//
    @GetMapping("/member/{id}")
    public String profile(
            @PathVariable Long id,
            MyStudyInviteForm form,
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
        if (rq.isLogin())
            if (member.getNickName().equals(rq.getMember().getNickName()))
                return "redirect:/member/profile/rank";

        // 로그인일 때만 스터디 리스트 model 에 전달
        if (rq.isLogin()) {
            List<MyStudy> myStudies = memberService.getMyStudyOnlyLeader(rq.getMember());
            model.addAttribute("myStudies", myStudies);
        }

        model.addAttribute("member", member);

        log.info("member 조회 성공 member name = {}", member.getNickName());
        return "member/member";
    }

    //-- member list --//
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        log.info("member list 요청 확인");
        Page<Member> paging = memberService.getAll(page);

        model.addAttribute("paging", paging);
        log.info("member list 응답 완료");
        return "member/list";
    }

    //-- name, about, img 수정 폼 --//
    @GetMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String modifyForm(
            MemberModifyForm form,
            Model model
    ) {
        Member member = rq.getMember();
        log.info("프로필 수정폼 요청 확인 member id = {}", member.getId());

        form.setNickName(member.getNickName());

        if (member.getAbout() != null)
            form.setAbout(member.getAbout());

        if (member.getProfileImg() != null)
            form.setProfileImg(member.getProfileImg());

        List<Integer> random = memberService.random();
        random.remove(0);

        model.addAttribute("random", random);
        log.info("프로필 수정폼 응답 완료");
        return "member/modify";
    }

    //-- 프로필 수정 처리 --//
    @PostMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String modify(MemberModifyForm form) {
        log.info("프로필 수정 처리 요청 확인 form = {}", form);

        RsData<Member> memberRs = memberService.modify(rq.getMember(), form);

        if (memberRs.isFail()) {
            log.info("프로필 수정 실패 msg = {}", memberRs.getMsg());
            return rq.historyBack(memberRs.getMsg());
        }

        log.info("프로필 수정 완료");
        return rq.redirectWithMsg("/member/profile", memberRs.getMsg());
    }

}
