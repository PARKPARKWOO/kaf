package com.baeker.baeker.studyRule;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.base.request.RsData;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class StudyRuleController {

    private StudyRuleService studyRuleService;
    private Rq rq;


    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public void modify(@PathVariable("id") Long id, @Valid StudyRuleForm studyRuleForm) {
        Optional<StudyRule> studyRule = studyRuleService.getStudyRule(id);


    }
}
