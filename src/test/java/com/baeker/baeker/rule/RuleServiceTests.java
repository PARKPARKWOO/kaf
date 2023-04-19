package com.baeker.baeker.rule;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.studyRule.StudyRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class RuleServiceTests {
    @Autowired
    private RuleService ruleService;

    @Autowired
    private RuleRepository ruleRepository;


    @Test
    @DisplayName(value = "단순 CRUD 테스트")
    void createTests() {
        //생성 메서드 //
        RuleForm ruleForm = new RuleForm("aaaa", "소개", 1,"bj", "BRONZE");
        RsData<Rule> rsData = ruleService.create(ruleForm);
        Rule rule = rsData.getData();
        assertThat(rule.getName()).isEqualTo("aaaa");
        System.out.println(rsData.getMsg());


        //수정 메서드 //
        RuleForm ruleForm1 = new RuleForm("wy9295", "소개2", 2,"boj2", "GOLD");
        Optional<Rule> optionalRule = ruleRepository.findByName("aaaa");
        if (optionalRule.isEmpty()) {
            System.out.println("실패 테스트임 !! 값이없다!!!!!!");
        }

        Rule rule1 = optionalRule.get();
        ruleService.modify(rule1, ruleForm1);

        assertThat(rule1.getName()).isEqualTo("wy9295");


        //조회 메서드 //

        Rule rs = ruleService.getRule("wy9295").getData();
        assertThat(rs.getDifficulty()).isEqualTo("GOLD");


        // 삭제 메서드 //
        Optional<Rule> opDelete = ruleRepository.findById(1L);
        opDelete.ifPresent(value -> ruleService.delete(value));
        assertThat(ruleService.getRule(1L).getData()).isNull();

    }

}
