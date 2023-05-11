package com.baeker.baeker.myStudy;

import com.baeker.baeker.member.Member;
import com.baeker.baeker.study.QStudy;
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
}
