package com.baeker.baeker.study;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.study.form.StudyCreateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.engine.Mode;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;
    private final Rq rq;

    //-- 스터디 생성 폼 --//
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createForm(StudyCreateForm form) {
        log.info("스터디 생성폼 요청 확인");
        return "/study/create";
    }

    //-- 스터디 생성 처리 --//
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String showCreate(StudyCreateForm form) {
        log.info("스터디 생성 요청 확인 form ={}", form.toString());
        Member member = rq.getMember();

        RsData<Study> studyRs = studyService.create(form, member);

        if (studyRs.isFail()) {
            log.info("중복검사 실패 study name = {}", form.getName());
            return rq.historyBack(studyRs);
        }

        Study study = studyRs.getData();
        log.info("스터디 생성 완료 study name = {}", study.getName());
        return rq.redirectWithMsg("/study/detail/" + study.getId(), "스터디 개설 완료!");
    }

    //-- 스터디 상세 페이지 --//
    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable Long id,
            Model model
    ) {
        log.info("스터디 상세페이지 요청 확인 study id = {}", id);
        RsData<Study> studyRs = studyService.getStudy(id);

        if (studyRs.isFail()) {
            log.info("존재하지 않는 id = {}", id);
            return rq.historyBack(studyRs);
        }

        model.addAttribute("study", studyRs.getData());
        log.info("상세페이지 응답 완료");
        return "/study/detail";
    }

    //-- 스터디 리스트 --//
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        log.info("study list 요청 확인");
        Page<Study> paging = studyService.getAll(page);

        model.addAttribute("paging", paging);
        log.info("study list 응답 완료");
        return "study/list";
    }
}






