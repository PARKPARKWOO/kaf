package com.baeker.baeker.study;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.myStudy.MyStudyService;
import com.baeker.baeker.study.form.StudyCreateForm;
import com.baeker.baeker.study.form.StudyModifyForm;
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
    private final MyStudyService myStudyService;
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
        myStudyService.create(member, study);

        log.info("스터디 생성 완료 study name = {}", study.getName());
        return rq.redirectWithMsg("/study/detail/" + study.getId(), "스터디 개설 완료!");
    }

    //-- 스터디 상세 페이지 --//
    @GetMapping("/detail/{list}/{id}")
    public String detail(
            @PathVariable String list,
            @PathVariable Long id,
            Model model
    ) {
        log.info("스터디 상세페이지 요청 확인 study id = {}", id);
        RsData<Study> studyRs = studyService.getStudy(id);

        if (studyRs.isFail()) {
            log.info("존재하지 않는 id = {}", id);
            return rq.historyBack(studyRs);
        }

        model.addAttribute("list", list);
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

    //-- 스터디 수정 폼 : 스터디명, 스터디 소개, 총 인원 --//
    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modifyForm(
            @PathVariable Long id,
            StudyModifyForm form
    ) {
        log.info("스터디 수정 폼 요청 확인 id = {}", id);
        RsData<Study> studyRs = studyService.getStudy(id);

        if (studyRs.isFail())
            return rq.historyBack(studyRs.getMsg());

        Study study = studyRs.getData();

        if (!study.getLeader().equals(rq.getMember().getNickName()))
            return rq.historyBack("수정 권한이 없습니다.");

        if (!study.getAbout().isEmpty())
            form.setAbout(study.getAbout());

        form.setId(id);
        form.setName(study.getName());
        form.setCapacity(study.getCapacity());

        log.info("스터디 수정 폼 응답 완료");
        return "study/modify";
    }

    //-- 스터디 수정 처리 --//
    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(
            @PathVariable Long id,
            StudyModifyForm form
    ) {
        log.info("스터디 수정 처리 요청 확인 id = {}", form.getId());
        RsData<Study> studyRs = studyService.getStudy(id);

        if (studyRs.isFail()){
            log.info("존재하지 않는 id 입니다. id = {}", id);
            return rq.historyBack(studyRs.getMsg());
        }

        Study study = studyRs.getData();
        if (!study.getLeader().equals(rq.getMember().getNickName()))
            return rq.historyBack("수정 권한이 없습니다.");

        RsData<Study> modifyStudyRs = studyService.modify(form, id);

        if (modifyStudyRs.isFail())
            return rq.historyBack(modifyStudyRs.getMsg());

        log.info("스터디 수정 처리 완료");
        return rq.redirectWithMsg("/study/detail/" + modifyStudyRs.getData().getId(), "수정이 완료되었습니다.");
    }
}






