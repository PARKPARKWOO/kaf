package com.baeker.baeker.myStudy;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.embed.BaekJoon;
import com.baeker.baeker.member.form.MemberJoinForm;
import com.baeker.baeker.study.Study;
import com.baeker.baeker.study.StudyService;
import com.baeker.baeker.study.form.StudyCreateForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MyStudyServiceTest {

    @Autowired MemberService memberService;
    @Autowired MyStudyService myStudyService;
    @Autowired StudyService studyService;

    private Member create(String username, String name) {
        MemberJoinForm form = new MemberJoinForm(username, name, "", "1234", "1234", 1);
        return memberService.join(form).getData();
    }

    private Study createStudy(String name, Member member) {
        StudyCreateForm form = new StudyCreateForm(name, "about", 7);
        Study study = studyService.create(form, member).getData();
        myStudyService.create(member, study);
        return study;
    }

    @Test
    void 스터디_가입_신청() {
        Member leader = create("user", "leader");
        Study study = createStudy("study", leader);

        Member member1 = create("user1", "member1");
        RsData<MyStudy> myStudyRs = myStudyService.join(member1, study, "hi");

        assertThat(myStudyRs.getResultCode()).isEqualTo("S-1");

        MyStudy myStudy = myStudyRs.getData();

        assertThat(myStudy.getStatus()).isSameAs(StudyStatus.PENDING);
        assertThat(myStudy.getMember().getNickName()).isEqualTo("member1");
        assertThat(myStudy.getStudy().getName()).isEqualTo("study");
        assertThat(myStudy.getStudy().getLeader()).isEqualTo(leader.getNickName());

        // 중복 가입 금지
        RsData<MyStudy> joinDuplicate = myStudyService.join(member1, study, "hello");
        assertThat(joinDuplicate.getResultCode()).isEqualTo("F-1");
    }

    @Test
    void 스터디로_초대() {
        Member leader = create("user", "leader");
        Study study = createStudy("study", leader);

        Member member1 = create("user1", "member1");
        Member member2 = create("user2", "member2");

        // 스터디 맴버가 아닌데 스터디 맴버 초대 금지
        RsData<MyStudy> invite = myStudyService.invite(member1, member2, study, "hi");
        assertThat(invite.getResultCode()).isEqualTo("F-1");

        // 정상적인 초대, 검증
        RsData<MyStudy> myStudyRs = myStudyService.invite(leader, member1, study, "hi");
        assertThat(myStudyRs.getResultCode()).isEqualTo("S-1");

        MyStudy myStudy = myStudyRs.getData();

        assertThat(myStudy.getStatus()).isSameAs(StudyStatus.INVITING);
        assertThat(myStudy.getStudy().getName()).isEqualTo("study");
        assertThat(myStudy.getMember().getNickName()).isEqualTo("member1");

        // 중복 초대 금지
        RsData<MyStudy> inviteDuplicate = myStudyService.invite(leader, member1, study, "hi");
        assertThat(inviteDuplicate.getResultCode()).isEqualTo("F-1");

        // 스터디 맴버로 승인되기전 초대 금지
        RsData<MyStudy> inviting = myStudyService.invite(member1, member2, study, "hi");
        assertThat(inviting.getResultCode()).isEqualTo("F-2");
    }

    @Test
    void 정식_스터디맴버로_승인() {
        Member leader = create("user", "leader");
        Study study = createStudy("study", leader);

        Member member1 = create("user1", "member1");
        Member member2 = create("user2", "member2");

        // member1 : 스터디 가입 신청
        RsData<MyStudy> myStudyRs1 = myStudyService.join(member1, study, "hi");
        assertThat(myStudyRs1.getResultCode()).isEqualTo("S-1");
        assertThat(myStudyRs1.getData().getStatus()).isEqualTo(StudyStatus.PENDING);

        // member1 : 정식 승인전 member2 초대 -> F-2
        RsData<MyStudy> invite1 = myStudyService.invite(member1, member2, study, "'hi");
        assertThat(invite1.getResultCode()).isEqualTo("F-2");

        // member1 : 가입 승인 PENDING -> MEMBER
        RsData<BaekJoon> accept1 = myStudyService.accept(myStudyRs1.getData());
        assertThat(accept1.getResultCode()).isEqualTo("S-1");
        assertThat(myStudyRs1.getData().getStatus()).isEqualTo(StudyStatus.MEMBER);
        assertThat(myStudyRs1.getData().getMember().getNickName()).isEqualTo("member1");

        // member1 : 스터디로 member2 초대
        // member2 : INVITING
        RsData<MyStudy> invite2 = myStudyService.invite(member1, member2, study, "hi");
        assertThat(invite2.getResultCode()).isEqualTo("S-1");
        assertThat(invite2.getData().getStatus()).isEqualTo(StudyStatus.INVITING);
        
        // member2 : 가입 승인 INVITING -> MEMBER
        RsData<BaekJoon> accept2 = myStudyService.accept(invite2.getData());
        assertThat(accept2.getResultCode()).isEqualTo("S-1");
        assertThat(invite2.getData().getStatus()).isEqualTo(StudyStatus.MEMBER);
        assertThat(invite2.getData().getMember().getNickName()).isEqualTo("member2");
    }

    @Test
    void 요청_메시지_수정() {
        Member leader = create("user", "leader");
        Study study = createStudy("study", leader);

        Member member1 = create("user1", "member1");
        RsData<MyStudy> myStudyRs = myStudyService.join(member1, study, "hi");
        MyStudy myStudy = myStudyRs.getData();

        assertThat(myStudyRs.getResultCode()).isEqualTo("S-1");
        assertThat(myStudy.getMsg()).isEqualTo("hi");

        // 메시지 수정
        MyStudy modifyMsg = myStudyService.modifyMsg(myStudy, "hello");

        assertThat(myStudy.getMsg()).isEqualTo("hello");
    }
}