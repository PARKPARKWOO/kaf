package com.baeker.baeker.studyRule;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.rule.RuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/studyRule")
@RequiredArgsConstructor
public class StudyRuleController {

    private final StudyRuleService studyRuleService;
    private final Rq rq;
    private final RuleService ruleService;

    /**
     * 생성
     */
    @GetMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showForm(){
        return "studyRule/create";
    }

    @PostMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String create(@PathVariable("id") Long id,@Valid StudyRuleForm studyRuleForm, BindingResult bindingResult) {
        RsData<StudyRule> rsData = studyRuleService.create(studyRuleForm, ruleService.getRule(id).getData());
        if (rsData.isFail() || bindingResult.hasErrors()) {
            return rq.historyBack(rsData);
        }
        return rq.redirectWithMsg("/studyRule/studyRuleList", rsData.getMsg());
    }

    /**
     * 수정
     */

    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showModify(@PathVariable("id") Long id, StudyRuleForm studyRuleForm) {
        RsData<StudyRule> rsData = studyRuleService.getStudyRule(id);
        if (rsData.isSuccess()) {
            StudyRule studyRule = rsData.getData();
            studyRuleService.setModify(studyRule, studyRuleForm);
            return "studyRule/create";
        } else {
            return rq.historyBack(rsData);
        }
    }

    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(@PathVariable("id") Long id, @Valid StudyRuleForm studyRuleForm, BindingResult bindingResult) {
        RsData<StudyRule> rsData = studyRuleService.getStudyRule(id);
        if (rsData.isFail() || bindingResult.hasErrors()) {
            return rq.historyBack(rsData);
        }
        studyRuleService.modify(rsData.getData(), studyRuleForm);
        return rq.redirectWithMsg("스터디 url", rsData.getMsg());
    }

    /**
     * 삭제
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable("id") Long id) {
        RsData<StudyRule> rsData = studyRuleService.getStudyRule(id);
        if (rsData.isSuccess()) {
            studyRuleService.delete(rsData.getData());
            return rq.redirectWithMsg("스터디 url", rsData);
        }
        return rq.historyBack(rsData);
    }

    /**
     * 조회
     */
    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList(Model model, @RequestParam(defaultValue = "0") int page){
        Page<StudyRule> paging = studyRuleService.getList(page);
        model.addAttribute("paging", paging);
        return "studyRule/studyRuleList";
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showDetail(@PathVariable("id") Long id, Model model){
        RsData<StudyRule> rsData = studyRuleService.getStudyRule(id);
        if (rsData.isSuccess()) {
            model.addAttribute("studyRule", rsData.getData());
            return "studyRule/detail";
        }
        return rq.historyBack(rsData);
    }
}
