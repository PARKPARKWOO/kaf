package com.baeker.baeker.member;

import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.myStudy.QMyStudy;
import com.baeker.baeker.study.QStudy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory query;

    public MemberQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //-- member 가 리더인 my study 찾기 --//
    public MyStudy findLeader(Member member) {
        QMyStudy myStudy = QMyStudy.myStudy;
        QStudy study = QStudy.study;

        List<MyStudy> leader = query
                .selectFrom(myStudy)
                .join(myStudy.study, study)
                .where(myStudy.member.eq(member)
                        .and(study.leader.eq(member.getNickName())))
                .fetch();
        return leader.get(0);
    }
}
