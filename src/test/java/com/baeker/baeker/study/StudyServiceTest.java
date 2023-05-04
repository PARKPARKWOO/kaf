package com.baeker.baeker.study;

import com.baeker.baeker.base.event.BaekJoonEvent;
import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.embed.BaekJoonDto;
import com.baeker.baeker.member.form.MemberJoinForm;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.myStudy.MyStudyService;
import com.baeker.baeker.myStudy.StudyStatus;
import com.baeker.baeker.study.form.StudyCreateForm;
import com.baeker.baeker.study.form.StudyModifyForm;
import com.baeker.baeker.study.snapshot.StudySnapShot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class StudyServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private StudyService studyService;
    @Autowired private MyStudyService myStudyService;
    @Autowired private ApplicationEventPublisher publisher;


    private Member create(String username, String name) {
        MemberJoinForm form = new MemberJoinForm(username, name, "", "1234", "1234", "");
        return memberService.join(form).getData();
    }

    private void connect(Member member, String baekJoonName) {
        BaekJoonDto dummy = new BaekJoonDto();
        RsData<Member> memberRsData = memberService.connectBaekJoon(member, baekJoonName, dummy);
    }

    private RsData<Study> createStudy(String name, String about, Integer capacity, Member member) {
        StudyCreateForm form = new StudyCreateForm(name, about, capacity);
        RsData<Study> studyRsData = studyService.create(form, member);

        Study study = studyRsData.getData();

        myStudyService.create(member, study);
        return studyRsData;
    }

//    @Test
    void 스터디_생성() {
        Member member = create("user", "member");
        connect(member, "Joon");
        RsData<Study> studyRs = createStudy("study1", "hi", 8, member);
        Long studyId = studyRs.getData().getId();

        Study study = studyService.getStudy(studyId).getData();

        assertThat(study).isSameAs(studyRs.getData());

        assertThat(studyRs.getResultCode()).isEqualTo("S-1");
        assertThat(study.getName()).isEqualTo("study1");
        assertThat(study.getCapacity()).isEqualTo(8);
        assertThat(study.getSnapShotList().size()).isEqualTo(7);

        MyStudy myStudy = study.getMyStudies().get(0);

        assertThat(study.getLeader()).isEqualTo(myStudy.getMember().getNickName());
        assertThat(myStudy.getStatus()).isSameAs(StudyStatus.MEMBER);
        assertThat(myStudy.getMember()).isSameAs(member);
        assertThat(member.getMyStudies().get(0)).isSameAs(myStudy);
    }

    @Test
    void 백준_연동_없이_스터디_생성_금지() {
        Member member = create("user", "member");

        assertThatThrownBy(() -> createStudy("study", "about", 10, member))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 이름_소개_인원_수정() {
        Member member = create("user", "member");
        connect(member, "Joon");
        RsData<Study> studyRs = createStudy("study1", "hi", 8, member);
        Study study = studyRs.getData();
        List<Study> studies = studyService.getAll();

        assertThat(study.getName()).isEqualTo("study1");
        assertThat(study.getAbout()).isEqualTo("hi");
        assertThat(study.getCapacity()).isEqualTo(8);
        assertThat(studies.size()).isEqualTo(1);

        StudyModifyForm form = new StudyModifyForm( study.getId(), "study2", "hello", 10);
        RsData<Study> modifyRs = studyService.modify(form, study.getId());
        studies = studyService.getAll();

        assertThat(modifyRs.getResultCode()).isEqualTo("S-1");
        assertThat(studies.size()).isEqualTo(1);

        Study modifyStudy = modifyRs.getData();

        assertThat(modifyStudy.getName()).isEqualTo("study2");
        assertThat(modifyStudy.getAbout()).isEqualTo("hello");
        assertThat(modifyStudy.getCapacity()).isEqualTo(10);
    }

    @Test
    void 리더_변경() {
        Member memberA = create("userA", "memberA");
        connect(memberA, "Joon");
        Member memberB = create("userB", "memberB");
        connect(memberB, "Baek");

        RsData<Study> studyRs = createStudy("study1", "hi", 8, memberA);
        Study study = studyRs.getData();

        assertThat(study.getLeader()).isEqualTo(memberA.getNickName());

        RsData<Study> modifyRs = studyService.modifyLeader(memberB, study.getId());

        assertThat(modifyRs.getResultCode()).isEqualTo("S-1");

        Study modifyStudy = modifyRs.getData();

        assertThat(modifyStudy.getLeader()).isEqualTo("memberB");
    }

//    @Test
    void 백준_이벤트_처리() {
        Member leader = create("user1", "leader");
        connect(leader, "Joon");
        Study study1 = createStudy("study1", "hi", 8, leader).getData();
        Study study2 = createStudy("study2", "hi", 8, leader).getData();

        BaekJoonDto dto = new BaekJoonDto(1, 1, 1, 1, 1, 1);
        publisher.publishEvent(new BaekJoonEvent(this, leader, dto));

        // leader 가 속한 모든 study 의 solved 변화 감지
        assertThat(study1.solvedBaekJoon()).isEqualTo(6);
        assertThat(study2.solvedBaekJoon()).isEqualTo(6);

        // 이벤트 처리로 추가된 스냅샷이 새로 생성 되지 않고 오늘 날짜에 잘 update 되었는지 확인
        String today = LocalDateTime.now().getDayOfWeek().toString().substring(0, 3);
        StudySnapShot snapShot = study1.getSnapShotList().get(0);

        assertThat(snapShot.getDayOfWeek()).isEqualTo(today);
        assertThat(study1.getSnapShotList().size()).isEqualTo(7);

        publisher.publishEvent(new BaekJoonEvent(this, leader, dto));

        assertThat(snapShot.getDayOfWeek()).isEqualTo(today);
        assertThat(study1.getSnapShotList().size()).isEqualTo(7);
        assertThat(study1.solvedBaekJoon()).isEqualTo(12);
    }
}