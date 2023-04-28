package com.baeker.baeker.studyRule;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.rule.Rule;
import com.baeker.baeker.rule.RuleService;
import com.baeker.baeker.study.Study;
import com.baeker.baeker.study.StudyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.List;

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
    public String showForm(Model model, @PathVariable Long id, StudyRuleForm studyRuleForm) {
        List<Rule> ruleList = ruleService.getRuleList();
        model.addAttribute("ruleList", ruleList);
        return "studyRule/create";
    }

    @PostMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String create(@PathVariable("id") Long id,@RequestParam("rule") Rule rule,
                         @Valid StudyRuleForm studyRuleForm, BindingResult bindingResult) {

        RsData<Study> checkLeader = studyRuleService.verificationLeader(rq, id);
        if (checkLeader.isFail()) {
            return rq.historyBack("스터디 리더가 아닙니다.");
        }

        RsData<StudyRule> rsData = studyRuleService.create(studyRuleForm, rule, checkLeader.getData());

        if (rsData.isFail() || bindingResult.hasErrors()) {
            return rq.historyBack(rsData);
        }
        return rq.redirectWithMsg(String.format("/study/detail/rule/%s", id), rsData.getMsg());
    }

    /**
     * 수정
     */

    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showModify(Model model,@PathVariable("id") Long id, StudyRuleForm studyRuleForm) {
        Long studyId = studyRuleService.getStudyId(id);

        RsData<Study> checkLeader = studyRuleService.verificationLeader(rq, studyId);
        if (checkLeader.isFail()) {
            return rq.historyBack("스터디 리더가 아닙니다.");
        }

        List<Rule> ruleList = ruleService.getRuleList();
        RsData<StudyRule> rsData = studyRuleService.getStudyRule(id);
        if (rsData.isSuccess()) {
            model.addAttribute("ruleList", ruleList);
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
        Long studyId = studyRuleService.getStudyId(id);

        RsData<Study> checkLeader = studyRuleService.verificationLeader(rq, studyId);
        if (checkLeader.isFail()) {
            return rq.historyBack("스터디 리더가 아닙니다.");
        }

        RsData<StudyRule> rsData = studyRuleService.getStudyRule(id);
        if (rsData.isFail() || bindingResult.hasErrors()) {
            return rq.historyBack(rsData);
        }
        studyRuleService.modify(rsData.getData(), studyRuleForm);
        return rq.redirectWithMsg(String.format("/studyRule/detail/%s", id), "규칙이 수정되었습니다");
    }

    /**
     * 삭제
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable("id") Long id) {
        Long studyId = studyRuleService.getStudyId(id);

        RsData<Study> checkLeader = studyRuleService.verificationLeader(rq, studyId);
        if (checkLeader.isFail()) {
            return rq.historyBack("스터디 리더가 아닙니다.");
        }

        RsData<StudyRule> rsData = studyRuleService.getStudyRule(id);
        if (studyRuleService.delete(rsData.getData(), rsData.getData().getStudy().getLeader(), rq.getMember().getNickName()).isSuccess()) {
            return rq.redirectWithMsg(String.format("/study/detail/rule/%s", studyId), "삭제 되었습니다.");
        }
        return rq.historyBack(rsData);
    }

    /**
     * 조회
     */
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
