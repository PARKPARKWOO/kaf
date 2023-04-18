package com.baeker.baeker.member;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.form.MemberJoinForm;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.myStudy.MyStudyService;
import com.baeker.baeker.study.Study;
import com.baeker.baeker.study.StudyService;
import com.baeker.baeker.study.form.StudyCreateForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private StudyService studyService;
    @Autowired private MyStudyService myStudyService;

    private Member create(String username, String name) {
        MemberJoinForm form = new MemberJoinForm(username, name, "", "1234", "1234");
        return memberService.join(form).getData();
    }

    private RsData<Study> createStudy(String name, Member member) {
        StudyCreateForm form = new StudyCreateForm(name, "about", 10);
        return studyService.create(form, member);
    }

    @Test
    void 회원가입() {
        MemberJoinForm form = new MemberJoinForm("username", "name",  "","1234", "1234");
        RsData<Member> memberRs = memberService.join(form);
        Member member = memberRs.getData();

        Member findMember = memberService.getMember("username").get();

        assertThat(memberRs.getResultCode()).isEqualTo("S-1");
        assertThat(member).isSameAs(findMember);
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    void 해결한_문제_수_업데이트() {

        // member1 : study 생성
        Member member1 = create("user1", "member1");
        Study study = createStudy("study", member1).getData();

        assertThat(member1.getSolvedCount()).isEqualTo(0);
        assertThat(study.getSolvedCount()).isEqualTo(0);

        // member1 : 해결한 문제수 5 로 업데이트
        memberService.updateSolve(member1.getId(), 5);

        // member 가 속한 study 까지 업데이트 성공
        assertThat(member1.getSolvedCount()).isEqualTo(5);
        assertThat(study.getSolvedCount()).isEqualTo(5);


        // member2 : 8문제 해결
        Member member2 = create("user2", "member2");
        memberService.updateSolve(member2.getId(), 8);
        assertThat(member2.getSolvedCount()).isEqualTo(8);

        // member2 : study 가입
        MyStudy myStudy = myStudyService.join(member2, study).getData();
        myStudyService.accept(myStudy);

        // 가입이 성공하면 member2 의 해결문제수가 study 에 자동 합산됨
        assertThat(study.getSolvedCount()).isEqualTo(13);
    }
}