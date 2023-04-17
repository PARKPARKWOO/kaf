package com.baeker.baeker.studyRule;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.rule.Rule;
import com.baeker.baeker.rule.RuleForm;
import com.baeker.baeker.rule.RuleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class StudyRuleServiceTests {

    @Autowired
    private StudyRuleRepository studyRuleRepository;

    @Autowired
    private StudyRuleService studyRuleService;

    @Autowired
    private RuleService ruleService;

    @Test
    @DisplayName(value = "단순 CRUD 테스트")
    void createTests() {
        //생성 메서드 //
        StudyRuleForm studyRuleForm = new StudyRuleForm("aaaa", "소개");
        RuleForm ruleForm = new RuleForm("name", "about", "provider", "gold");
        Rule rule = ruleService.create(ruleForm).getData();
        RsData<StudyRule> rsData = studyRuleService.create(studyRuleForm, rule);
        StudyRule studyRule = rsData.getData();
        assertThat(studyRule.getName()).isEqualTo("aaaa");
        System.out.println(rsData.getMsg());


        //수정 메서드 //
        StudyRuleForm ruleForm1 = new StudyRuleForm("wy9295", "소개2");
        Optional<StudyRule> optionalRule = studyRuleRepository.findById(1L);
        if (optionalRule.isEmpty()) {
            System.out.println("실패 테스트임 !! 값이없다!!!!!!");
        }

        StudyRule studyRule1 = optionalRule.get();
        studyRuleService.modify(studyRule1, ruleForm1);

        assertThat(studyRule1.getName()).isEqualTo("wy9295");
        // 삭제 메서드 //
        Optional<StudyRule> opDelete = studyRuleRepository.findById(1L);
        opDelete.ifPresent(value -> studyRuleService.delete(value));
        assertThat(studyRuleService.getStudyRule(1L).getData()).isNull();
    }
}
