package com.baeker.baeker.rule;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.base.request.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/rule")
@RequiredArgsConstructor
public class RuleController {

    private final RuleService ruleService;

    private final Rq rq;


    // 생성 //
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String showCreate(RuleForm ruleForm) {
        return "rule/rule";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(@Valid RuleForm ruleForm, BindingResult bindingResult) {
        RsData<Rule> rsData = ruleService.create(ruleForm);
        if (rsData.isFail() || bindingResult.hasErrors()) {
            return rq.historyBack(rsData);
        }
        return rq.redirectWithMsg("스터디페이지", rsData.getMsg());
    }


    // 수정 //
    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showModify(@PathVariable("id") Long id, RuleForm ruleForm) {
        RsData<Rule> rsData = this.ruleService.getRule(id);
        if (rsData.isSuccess()) {
            Rule rule = rsData.getData();
            ruleService.setModify(rule, ruleForm);
            return "rule/rule";
        } else {
            return rq.historyBack(rsData);
        }
    }


    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(@PathVariable("id") Long id, @Valid RuleForm ruleForm, BindingResult bindingResult) {
        RsData<Rule> rsData = ruleService.getRule(id);
        if (rsData.isFail() || bindingResult.hasErrors()) {
            return rq.historyBack(rsData);
        }
        ruleService.modify(rsData.getData(), ruleForm);
        return rq.redirectWithMsg("스터디 url", rsData.getMsg());
    }

    // 삭제 //
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable("id") Long id) {
        RsData<Rule> rsData = ruleService.getRule(id);
        if (rsData.isSuccess()) {
            ruleService.delete(rsData.getData());
            return rq.redirectWithMsg("스터디 url", rsData.getMsg());
        } else {
            return rq.historyBack(rsData.getMsg());
        }
    }
}
