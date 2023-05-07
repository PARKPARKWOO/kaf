package com.baeker.baeker.base.initDB;

import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.embed.BaekJoonDto;
import com.baeker.baeker.member.form.MemberJoinForm;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.myStudy.MyStudyService;
import com.baeker.baeker.rule.RuleForm;
import com.baeker.baeker.rule.RuleService;
import com.baeker.baeker.study.Study;
import com.baeker.baeker.study.StudyService;
import com.baeker.baeker.study.form.StudyCreateForm;
import com.baeker.baeker.studyRule.StudyRuleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.init_alpha_and_study();
        initService.init_beta_and_study();
        initService.init_dummy_user();
        initService.initData();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberService memberService;
        private final StudyService studyService;
        private final MyStudyService myStudyService;

        private final RuleService ruleService;


        public void init_alpha_and_study() {

            // user 알파와 알파의 스터디 생성
            Member alpha = createMember("user1", "알파", "안녕하세요 알파입니다.", "https://avatars.dicebear.com/api/avataaars/600.svg", "alpha");

            Study study = createStudy("알파의 스터디", "Provident cupiditate voluptatem et in.", 10, alpha);
            createStudy("알파알파", "Provident cupiditate voluptatem et in.", 10, alpha);

            // 스넵샷 강제 생성 (총 문제 풀이 수 반영 x)
            for (int i = 6; i >= 0; i--) {

                int num = (int) (Math.random() * 5);
                BaekJoonDto dto = new BaekJoonDto(num, num, num, num, num, num);

                String dayOfWeek = LocalDateTime.now().minusDays(i).getDayOfWeek().toString();

                memberService.initDbSnapshotCreate(alpha, dto, dayOfWeek);
            }


            // 더미 user 3명 생성후 알파 스터디 가입
            for (int i = 3; i < 10; i++) {
                Member member = createMember("user" + i, "member" + i, "안녕하세요" + i, "https://avatars.dicebear.com/api/avataaars/" + i * 10 + ".svg", "Joon" + i);
                MyStudy myStudy = myStudyService.join(member, study, "hi").getData();
                myStudyService.accept(myStudy);
                studyService.addBaekJoon(myStudy.getStudy(), myStudy.getMember());
            }
        }

        public void init_beta_and_study() {

            // user 베타와 베타의 스터디 생성
            Member beta = createMember("user2", "베타", "안녕하세요 베타입니다.", "https://avatars.dicebear.com/api/avataaars/200.svg", "beta");
            Study study1 = createStudy("베타의 스터디", "Quaerat voluptatem et cupiditate in.", 10, beta);
            Study study2 = createStudy("베타베타", "Quaerat voluptatem et cupiditate in.", 10, beta);

            // 베타가 알파 초대
            Member alpha = memberService.getMember(1L).getData();
            myStudyService.invite(beta, alpha, study1, "초대합니다.");

            // 알파가 스터디 가입 요청
            myStudyService.join(alpha, study2, "가입 원해요.");
        }

        public void init_dummy_user() {
            // 스터디가 없는 더미 유저 6명 생성

            // 백준 아이디 연동 O
            for (int i = 0; i < 3; i++)
                createMember("dummy" + i, "dummy" + i, "안녕하세요" + i, "https://avatars.dicebear.com/api/avataaars/" + i * 50 + ".svg", "Baek" + i);

            // 백준 아이디 연동 X
            for (int i = 3; i < 6; i++)
                createMember("dummy" + i, "dummy" + i, "안녕하세요" + i, "https://avatars.dicebear.com/api/avataaars/" + i * 50 + ".svg", null);
        }

        public void initData() {

            // rule 목록확인
            RuleForm ruleForm = new RuleForm("이름", "소개", "1", "백준", "GOLD");

            for (int i = 0; i < 15; i++) {
                ruleService.create(ruleForm);
            }
        }

        private Member createMember(String username, String nickName, String about, String img, String baekJoonName) {
            BaekJoonDto dto = new BaekJoonDto(3, 3, 3, 3, 3, 3);
            return memberService.initDbMemberCreate("BAEKER", username, nickName, about, "1234", img, baekJoonName, dto);
        }

        private Study createStudy(String name, String about, Integer capacity, Member member) {
            StudyCreateForm form = new StudyCreateForm(name, about, capacity);
            Study study = studyService.create(form, member).getData();
            myStudyService.create(member, study);
            studyService.addBaekJoon(study, member);
            return study;
        }
    }

}
