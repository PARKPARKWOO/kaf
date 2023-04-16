package com.baeker.baeker.member;

import com.baeker.baeker.base.request.Rq;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.baeker.baeker.member.Member.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private MemberService memberService;
    @Autowired private Rq rq;


//    @Test
//    void 로그인_처리() throws Exception {
//        createMember();
//        // WHEN
//        ResultActions resultActions = mvc
//                .perform(post("/member/login")
//                        .with(csrf()) // CSRF 키 생성
//                        .param("username", "user1")
//                        .param("password", "1234")
//                )
//                .andDo(print());
//
//        // 세션에 접근해서 user 객체를 가져온다.
//        MvcResult mvcResult = resultActions.andReturn();
//        HttpSession session = mvcResult.getRequest().getSession(false);// 원래 getSession 을 하면, 만약에 없을 경우에 만들어서라도 준다., false 는 없으면 만들지 말라는 뜻
//        SecurityContext securityContext = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
//        User user = (User)securityContext.getAuthentication().getPrincipal();
//
//        assertThat(user.getUsername()).isEqualTo("user1");
//
//        // THEN
//        resultActions
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrlPattern("/**"));
//    }
}