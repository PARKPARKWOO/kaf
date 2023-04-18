package com.baeker.baeker.base.initDB;

import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.form.MemberJoinForm;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.myStudy.MyStudyService;
import com.baeker.baeker.study.Study;
import com.baeker.baeker.study.StudyService;
import com.baeker.baeker.study.form.StudyCreateForm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Configuration
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
        private final StudyService studyService;
        private final MyStudyService myStudyService;

        public void initData() {

            // 맴버 "알파" 생성
            MemberJoinForm memberForm = new MemberJoinForm("user1", "알파", "안녕하세요 알파입니다.", "1234", "1234");
            Member member = memberService.join(memberForm).getData();

            // 알파가 스터디 "Baeker lover" 생성
            StudyCreateForm studyFrom = new StudyCreateForm("Baeker lover", "Provident cupiditate voluptatem et in.", 10);
            Study study = studyService.create(studyFrom, member).getData();

            // init 맴버 8명 생성후 "Baeker lover" 에 가입
            for (int i = 2; i < 10; i++) {
                Member memberA = memberService.join(new MemberJoinForm("user" + i, "member" + i, "hello", "1234", "1234")).getData();
                MyStudy myStudy = myStudyService.join(memberA, study).getData();
                myStudyService.accept(myStudy);
            }
        }

    }
}
