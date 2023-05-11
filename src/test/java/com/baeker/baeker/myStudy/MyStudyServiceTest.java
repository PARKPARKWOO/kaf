package com.baeker.baeker.myStudy;

import com.baeker.baeker.base.event.BaekJoonEvent;
import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.embed.BaekJoonDto;
import com.baeker.baeker.member.form.MemberJoinForm;
import com.baeker.baeker.study.Study;
import com.baeker.baeker.study.StudyService;
import com.baeker.baeker.study.form.StudyCreateForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MyStudyServiceTest {

    @Autowired MemberService memberService;
    @Autowired MyStudyService myStudyService;
    @Autowired ApplicationEventPublisher publisher;
    @Autowired StudyService studyService;

    private Member create(String username, String name) {
        MemberJoinForm form = new MemberJoinForm(username, name, "", "1234", "1234", "");
        Member member = memberService.join(form).getData();

        memberService.connectBaekJoon(member, name);
        BaekJoonDto dummy = new BaekJoonDto(1,1,1,1,1,1);
        publisher.publishEvent(new BaekJoonEvent(this, member, dummy));
        return member;
    }
    
    private Study createStudy(String name, Member member) {
        StudyCreateForm form = new StudyCreateForm(name, "about", 7);
        Study study = studyService.create(form, member).getData();
        myStudyService.create(member, study);
        studyService.addBaekJoon(study, member);
        return study;
    }

    private void joinStudy(Member member1, Study study2) {
        MyStudy myStudy = myStudyService.join(member1, study2, "").getData();
        myStudyService.accept(myStudy);
    }

    @Test
    void 스터디_가입_신청() {
        Member leader = create("user", "leader");
        Study study = createStudy("study", leader);

        assertThat(study.solvedBaekJoon()).isEqualTo(6);

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

        // 승인 전에는 solved 가 반영되지 않음
        assertThat(study.solvedBaekJoon()).isEqualTo(6);
    }

    @Test
    void 스터디로_초대() {
        Member leader = create("user", "leader");
        Study study = createStudy("study", leader);

        assertThat(study.solvedBaekJoon()).isEqualTo(6);

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

        // 승인 전에는 solved 가 반영되지 않음
        assertThat(study.solvedBaekJoon()).isEqualTo(6);
    }

    @Test
    void 정식_스터디맴버로_승인() {
        Member leader = create("user", "leader");
        Study study = createStudy("study", leader);

        assertThat(study.solvedBaekJoon()).isEqualTo(6);

        Member member1 = create("user1", "member1");
        Member member2 = create("user2", "member2");

        // member1 : 스터디 가입 신청
        RsData<MyStudy> myStudyRs1 = myStudyService.join(member1, study, "hi");
        MyStudy myStudy1 = myStudyRs1.getData();


        assertThat(myStudyRs1.getResultCode()).isEqualTo("S-1");
        assertThat(myStudy1.getStatus()).isEqualTo(StudyStatus.PENDING);

        // member1 : 정식 승인전 member2 초대 -> F-2
        RsData<MyStudy> invite1 = myStudyService.invite(member1, member2, study, "'hi");
        assertThat(invite1.getResultCode()).isEqualTo("F-2");


        // member1 : 가입 승인 PENDING -> MEMBER
        RsData<MyStudy> accept1 = myStudyService.accept(myStudy1);
        studyService.addBaekJoon(myStudy1.getStudy(), myStudy1.getMember());

        assertThat(accept1.getResultCode()).isEqualTo("S-1");
        assertThat(myStudy1.getStatus()).isEqualTo(StudyStatus.MEMBER);
        assertThat(myStudy1.getMember().getNickName()).isEqualTo("member1");
        // solved count 반영
        assertThat(study.solvedBaekJoon()).isEqualTo(12);


        // member1 : 스터디로 member2 초대
        // member2 : INVITING
        RsData<MyStudy> invite2 = myStudyService.invite(member1, member2, study, "hi");
        MyStudy myStudy2 = invite2.getData();

        assertThat(invite2.getResultCode()).isEqualTo("S-1");
        assertThat(myStudy2.getStatus()).isEqualTo(StudyStatus.INVITING);
        
        // member2 : 가입 승인 INVITING -> MEMBER
        RsData<MyStudy> accept2 = myStudyService.accept(myStudy2);
        studyService.addBaekJoon(myStudy2.getStudy(), myStudy2.getMember());

        assertThat(accept2.getResultCode()).isEqualTo("S-1");
        assertThat(myStudy2.getStatus()).isEqualTo(StudyStatus.MEMBER);
        assertThat(myStudy2.getMember().getNickName()).isEqualTo("member2");
        // solved count 반영
        assertThat(study.solvedBaekJoon()).isEqualTo(18);
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

    @Test
    void Member_가_Leader_인_MyStudy_조회() {
        Member member1 = create("user1", "member1");
        Member member2 = create("user2", "member2");
        Member member3 = create("user3", "member3");

        Study study1 = createStudy("study1", member1);
        Study study1_1 = createStudy("study1_1", member1);
        Study study2 = createStudy("study2", member2);
        Study study3 = createStudy("study3", member3);

        joinStudy(member1, study2);
        joinStudy(member1, study3);

        // member1 이 가입한 스터디 수 확인
        assertThat(member1.getMyStudies().size()).isEqualTo(4);

        // member 가 리더인 my study 만 조회
        RsData<List<MyStudy>> leaderRs = myStudyService.getMyStudyOnlyLeader(member1);
        assertThat(leaderRs.isSuccess()).isTrue();
        List<MyStudy> myStudies = leaderRs.getData();

        // 검증
        assertThat(myStudies.size()).isEqualTo(2);
        assertThat(myStudies.contains(study1.getMyStudies().get(0))).isTrue();
        assertThat(myStudies.contains(study1_1.getMyStudies().get(0))).isTrue();

        // 리더가 아닌 my study 검증
        List<MyStudy> notLeaderMyStudy = study2.getMyStudies();
        for (MyStudy myStudy : notLeaderMyStudy)
            assertThat(myStudies.contains(myStudy)).isFalse();
    }


}