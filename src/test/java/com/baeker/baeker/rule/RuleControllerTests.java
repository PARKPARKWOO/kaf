package com.baeker.baeker.rule;

import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.form.MemberJoinForm;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class RuleControllerTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RuleController ruleController;
    @Autowired
    private RuleService ruleService;

    @Autowired
    private MemberService memberService;

    @Test
    @Rollback(value = false)
    @DisplayName("user10 생성")
    void create() {
        MemberJoinForm memberJoinForm = new MemberJoinForm("user10", "user10", "소개", "1234", "1234", 1);
        Member member = memberService.join(memberJoinForm).getData();
    }

    @Test
    @WithAnonymousUser
    @DisplayName("로그인 안했을때 create")
    void notLogin() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/rule/create"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(RuleController.class))
                .andExpect(handler().methodName("showCreate"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/member/login**"));
    }


    @Test
    @WithUserDetails("user10")
    @DisplayName("로그인 했을때 create")
    void createTests() throws Exception {


        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/rule/create"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(RuleController.class))
                .andExpect(handler().methodName("showCreate"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        <input type="text" name="name" placeholder="규칙 이름"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="text" name="about" placeholder="간단 소개"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="text" name="provider" placeholder="OJ 사이트"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="difficulty" value="ALL"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="difficulty" value="BRONZE"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="difficulty" value="SILVER"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="difficulty" value="GOLD"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="difficulty" value="PLATINUM"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="difficulty" value="DIAMOND"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="difficulty" value="RUBY"
                        """.stripIndent().trim())));
    }

    @Test
    @DisplayName("create Form POST")
    @WithUserDetails("user10")
    void createForm() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/rule/create")
                        .with(csrf()) // CSRF 키 생성
                        .param("name", "rule")
                        .param("about", "안녕")
                        .param("xp", "1")
                        .param("difficulty", "GOLD")
                        .param("provider", "BJ")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(RuleController.class))
                .andExpect(handler().methodName("create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/study/**"));

        Rule rule = ruleService.getRule("rule").getData();

        // StudyRule 추가해야함

        assertThat(rule.getName()).isEqualTo("rule");
    }
}

