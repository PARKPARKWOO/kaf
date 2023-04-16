package com.baeker.baeker.member;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.form.MemberJoinForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired private MemberService memberService;



    @Test
    void 회원가입() {
        MemberJoinForm form = new MemberJoinForm("username", "name", "1234", "1234");
        RsData<Member> memberRs = memberService.join(form);
        Member member = memberRs.getData();

        Member findMember = memberService.getMember("username").get();

        assertThat(memberRs.getResultCode()).isEqualTo("S-1");
        assertThat(member).isSameAs(findMember);
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }
}