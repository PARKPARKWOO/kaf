package com.baeker.baeker.studyRule.api;


import com.baeker.baeker.rule.RuleService;
import com.baeker.baeker.study.StudyService;
import com.baeker.baeker.studyRule.StudyRule;
import com.baeker.baeker.studyRule.StudyRuleService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/study-rule")
public class StudyRuleApiController {
    private final StudyRuleService studyRuleService;
    private final StudyService studyService;
    private final RuleService ruleService;

    @GetMapping("/search")
    public Result searchAll() {
        List<StudyRule> studyRules = studyRuleService.getAll();
        List<StudyRuleDto> dto = studyRules.stream()
                .map(m -> new StudyRuleDto(m.getStudy().getName(), m.getName(), m.getAbout()))
                .toList();
        return new Result(dto.size(), dto);
    }

    @GetMapping("/search/{id}")
    public Result searchStudyRule(@PathVariable("id") Long id) {
        List<StudyRule> studyRules = studyRuleService.getAll();
        List<StudyRuleDto> dto = studyRules.stream()
                .filter(m -> m.getStudy().getId().equals(id))
                .map(m -> new StudyRuleDto(m.getStudy().getName(), m.getName(), m.getAbout()))
                .toList();
        return new Result(dto.size(), dto);
    }


    /**
     * DTO Request
     */


    /**
     * DTO Response
     */
    @AllArgsConstructor
    @Data
    static class Result<T> {
        private int count;
        private T data;
    }

    /**
     * DTO
     */
    @AllArgsConstructor
    @Data
    static class StudyRuleDto {
        private String studyName;
        private String name;
        private String about;
    }

}

