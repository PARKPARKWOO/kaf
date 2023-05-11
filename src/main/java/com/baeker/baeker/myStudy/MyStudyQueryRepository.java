package com.baeker.baeker.myStudy;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.study.QStudy;
import com.baeker.baeker.study.Study;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MyStudyQueryRepository {

    private final JPAQueryFactory query;

    public MyStudyQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //-- member 가 리더인 my study 찾기 --//
    public List<MyStudy> findLeader(Member member) {
        QMyStudy myStudy = QMyStudy.myStudy;
        QStudy study = QStudy.study;

        return query
                .selectFrom(myStudy)
                .join(myStudy.study, study)
                .where(myStudy.member.eq(member)
                        .and(study.leader.eq(member.getNickName())))
                .fetch();
    }

    //-- MEMBER 등급 my study 조회 by Member--//
    public List<MyStudy> statusMember(Member member) {
        QMyStudy myStudy = QMyStudy.myStudy;

        return query
                .selectFrom(myStudy)
                .where(myStudy.member.eq(member)
                        .and(myStudy.status.eq(StudyStatus.MEMBER)))
                .fetch();
    }

    //-- MEMBER 등급 my study 조회 by Study--//
    public List<MyStudy> statusMember(Study study) {
        QMyStudy myStudy = QMyStudy.myStudy;

        return query
                .selectFrom(myStudy)
                .where(myStudy.study.eq(study)
                        .and(myStudy.status.eq(StudyStatus.MEMBER)))
                .fetch();
    }

    //-- member 와 study 가 일치하는 My Study 조회 --//
    public List<MyStudy> getMyStudy(Member member, Study study) {
        QMyStudy myStudy = QMyStudy.myStudy;

        return query
                .selectFrom(myStudy)
                .where(myStudy.member.eq(member)
                        .and(myStudy.study.eq(study)))
                .fetch();
    }
}
