package com.baeker.baeker.rule;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.base.request.RsData;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/rule")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private Rq rq;

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public String showCreate() {
        return "Rule주소";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(@Valid RuleForm ruleForm, BindingResult bindingResult) {
        RsData<Rule> rsData = ruleService.create(ruleForm.getName(), ruleForm.getAbout(), ruleForm.getProvider(), ruleForm.getDifficulty());
        if (rsData.isFail()) {
            return rq.historyBack(rsData);
        }
        return rq.redirectWithMsg("스터디페이지", "스터디 규칙이 생성 되었습니다.");
    }


    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showModify(@PathVariable("id") Long id) {
        Optional<Rule> checkRule = ruleService.getRule(id);
        if (checkRule.isPresent()) {
            Rule rule = checkRule.get();
            RuleForm ruleForm = new RuleForm(rule.getName(), rule.getAbout(), rule.getProvider(), rule.getDifficulty());
            return "rule/ruleForm";
        } else {
            return rq.historyBack("규칙이 없습니다.");
        }
    }


    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(@PathVariable("id") Long id, @Valid RuleForm ruleForm, BindingResult bindingResult) {
        Optional<Rule> checkRule = ruleService.getRule(id);
        if (checkRule.isPresent() && !bindingResult.hasErrors()) {
            ruleService.modify(checkRule.get(), ruleForm.getName(), ruleForm.getAbout(), ruleForm.getProvider(), ruleForm.getDifficulty());
            return "스터디페이지";
        } else {
            return rq.historyBack("규칙을 확인해주세요");
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String  delete(@PathVariable("id") Long id) {
        Optional<Rule> checkRule = ruleService.getRule(id);
        if (checkRule.isPresent()) {
            ruleService.delete(checkRule.get());
            return "스터디 html";
        } else {
            return rq.historyBack("규칙이 없습니다.");
        }
    }
}
