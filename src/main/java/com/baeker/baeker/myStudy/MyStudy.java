package com.baeker.baeker.myStudy;

import com.baeker.baeker.base.entity.BaseEntity;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.study.Study;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class MyStudy extends BaseEntity {

    private String msg;

    @Enumerated(EnumType.STRING)
    private StudyStatus status;

    @ManyToOne(fetch = LAZY)
    private Member member;

    @ManyToOne(fetch = LAZY)
    private Study study;

    //-- create method --//

    // 새로운 스터디 만들 때 //
    public static MyStudy createNewStudy(Member member, Study study) {
        MyStudy myStudy = create(member, study);
        myStudy.status = StudyStatus.MEMBER;

        member.getMyStudies().add(myStudy);
        study.getMyStudies().add(myStudy);

        return myStudy;
    }

    // 스터디 가입할 때 //
    protected static MyStudy joinStudy(Member member, Study study, String msg) {
        MyStudy myStudy = create(member, study);
        myStudy.status = StudyStatus.PENDING;
        myStudy.msg = msg;

        member.getMyStudies().add(myStudy);
        study.getMyStudies().add(myStudy);

        return myStudy;
    }

    // 스터디로 초대할 때 //
    protected static MyStudy inviteStudy(Member member, Study study, String msg) {
        MyStudy myStudy = create(member, study);
        myStudy.status = StudyStatus.INVITING;
        myStudy.msg = msg;

        member.getMyStudies().add(myStudy);
        study.getMyStudies().add(myStudy);

        return myStudy;
    }

    // my study 생성 //
    private static MyStudy create(Member member, Study study) {
        return builder()
                .member(member)
                .study(study)
                .build();
    }


    //-- business logic --//

    // 가입, 초대 신청 승인 //
    protected void accept() {
        this.status = StudyStatus.MEMBER;
        this.msg = null;
    }

    // 초대, 가입 요청 메시지 변경 //
    protected MyStudy modifyMsg(String msg) {
        return this.toBuilder()
                .msg(msg)
                .build();
    }

}
