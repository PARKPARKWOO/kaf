package com.baeker.baeker.studyRule;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.rule.Rule;
import com.baeker.baeker.studyRule.solvedApi.SolvedApiManager;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/study_rule")
public class StudyRuleController {

    private StudyRuleService studyRuleService;
    private Rq rq;

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(StudyRuleForm studyRuleForm) {
        return "studyRule/studyRule";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(@Valid StudyRuleForm studyRuleForm, BindingResult bindingResult, Rule rule) {
        RsData<StudyRule> rsData = studyRuleService.create(studyRuleForm, rule);
        if (rsData.isFail() || bindingResult.hasErrors()) {
            return rq.historyBack(rsData);
        }
        SolvedApiManager solvedApiManager = new SolvedApiManager(rsData.getData().getRule().getDifficulty());
        return rq.redirectWithMsg("스터디페이지", rsData.getMsg());
    }


    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showModify(@PathVariable("id") Long id, StudyRuleForm studyRuleForm) {
        RsData<StudyRule> rsData = studyRuleService.getStudyRule(id);
        if (rsData.isSuccess()) {
            StudyRule studyRule = rsData.getData();
            studyRuleService.setModify(studyRule, studyRuleForm);
            return "studyRule/studyRule";
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
}
