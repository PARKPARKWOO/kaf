package com.baeker.baeker.study;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.form.MemberJoinForm;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.myStudy.StudyStatus;
import com.baeker.baeker.study.form.StudyCreateForm;
import com.baeker.baeker.study.form.StudyModifyForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class StudyServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private StudyService studyService;


    private Member create(String username, String name) {
        MemberJoinForm form = new MemberJoinForm(username, name, "", "1234", "1234");
        return memberService.join(form).getData();
    }

    private RsData<Study> createStudy(String name, String about, Integer capacity, Member member) {
        StudyCreateForm form = new StudyCreateForm(name, about, capacity);
        return studyService.create(form, member);
    }

    @Test
    void 스터디_생성() {
        Member member = create("user", "member");
        RsData<Study> studyRs = createStudy("study1", "hi", 8, member);
        Long studyId = studyRs.getData().getId();

        Study study = studyService.getStudy(studyId).getData();

        assertThat(study).isSameAs(studyRs.getData());

        assertThat(studyRs.getResultCode()).isEqualTo("S-1");
        assertThat(study.getName()).isEqualTo("study1");
        assertThat(study.getCapacity()).isEqualTo(8);

        MyStudy myStudy = study.getMyStudies().get(0);

        assertThat(study.getLeader()).isEqualTo(myStudy.getMember().getName());
        assertThat(myStudy.getStatus()).isSameAs(StudyStatus.MEMBER);
        assertThat(myStudy.getMember()).isSameAs(member);
        assertThat(member.getMyStudies().get(0)).isSameAs(myStudy);
    }

    @Test
    void 이름_소개_인원_수정() {
        Member member = create("user", "member");
        RsData<Study> studyRs = createStudy("study1", "hi", 8, member);
        Study study = studyRs.getData();
        List<Study> studies = studyService.getAll();

        assertThat(study.getName()).isEqualTo("study1");
        assertThat(study.getAbout()).isEqualTo("hi");
        assertThat(study.getCapacity()).isEqualTo(8);
        assertThat(studies.size()).isEqualTo(1);

        StudyModifyForm form = new StudyModifyForm("study2", "hello", 10);
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
        Member memberB = create("userB", "memberB");
        RsData<Study> studyRs = createStudy("study1", "hi", 8, memberA);
        Study study = studyRs.getData();

        assertThat(study.getLeader()).isEqualTo(memberA.getName());

        RsData<Study> modifyRs = studyService.modifyLeader(memberB, study.getId());

        assertThat(modifyRs.getResultCode()).isEqualTo("S-1");

        Study modifyStudy = modifyRs.getData();

        assertThat(modifyStudy.getLeader()).isEqualTo("memberB");
    }


}