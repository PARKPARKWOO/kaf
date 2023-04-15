package com.baeker.baeker.base.initDB;

import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.form.MemberJoinForm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//@Configuration
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initData();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberService memberService;

        public void initData() {
            MemberJoinForm form = new MemberJoinForm("userId", "name", "1234", "1234");
            Member member = memberService.join(form).getData();
        }
    }
}
