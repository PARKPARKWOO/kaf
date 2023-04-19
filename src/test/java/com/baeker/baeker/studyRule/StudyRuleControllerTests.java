package com.baeker.baeker.studyRule;

import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.form.MemberJoinForm;
import com.baeker.baeker.rule.Rule;
import com.baeker.baeker.rule.RuleController;
import com.baeker.baeker.rule.RuleForm;
import com.baeker.baeker.rule.RuleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class StudyRuleControllerTests {
    @Autowired
    private StudyRuleController studyRuleController;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private StudyRuleService studyRuleService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RuleService ruleService;

    @Test
    @Rollback(value = false)
    @DisplayName("user11 생성")
    void create() {
        MemberJoinForm memberJoinForm = new MemberJoinForm("user11", "user11", "소개", "1234","1234", 1);
        Member member = memberService.join(memberJoinForm).getData();

        RuleForm ruleForm = new RuleForm("name", "about", "1", "provider", "GOLD");
        Rule rule = ruleService.create(ruleForm).getData();
    }

    @Test
    @WithAnonymousUser
    @DisplayName("로그인 안했을때 create")
    void notLogin() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/studyrule/create"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(StudyRuleController.class))
                .andExpect(handler().methodName("create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/member/login**"));
    }
    @Test
    @WithUserDetails("user11")
    @DisplayName("로그인 했을때 create")
    void createTests() throws Exception {


        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/studyrule/create"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(StudyRuleController.class))
                .andExpect(handler().methodName("create"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        <input type="text" name="name" placeholder="규칙 이름"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="text" name="about" placeholder="간단 소개"
                        """.stripIndent().trim())));

    }
//    @Test
//    @DisplayName("create Form POST")
//    @WithUserDetails("user11")
//    void createForm() throws Exception {
//        // WHEN
//        ResultActions resultActions = mvc
//                .perform(post("/studyrule/create")
//                        .with(csrf()) // CSRF 키 생성
//                        .param("name", "studyRule")
//                        .param("about", "안녕")
//
//                )
//                .andDo(print());
//
//        // THEN
//        resultActions
//                .andExpect(handler().handlerType(StudyRuleController.class))
//                .andExpect(handler().methodName("create"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrlPattern("/study/**"));
//
//        StudyRule studyRule = studyRuleService.getStudyRule("studyRule").getData();
//
//        assertThat(studyRule.getName()).isEqualTo("studyRule");
//    }
}
