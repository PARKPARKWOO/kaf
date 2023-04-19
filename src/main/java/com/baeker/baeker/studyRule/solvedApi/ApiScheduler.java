package com.baeker.baeker.studyRule.solvedApi;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.embed.BaekJoon;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.rule.Rule;
import com.baeker.baeker.study.Study;
import com.baeker.baeker.study.StudyService;
import com.baeker.baeker.studyRule.StudyRule;
import com.baeker.baeker.studyRule.StudyRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class ApiScheduler {

    private final SolvedApiManager solvedApiManager;

    private final SolvedApiService solvedApiService;

    private final StudyRuleService studyRuleService;

    private final StudyService studyService;

    private final MemberService memberService;

    /**
     * 티어 체크 후 Study
     */
//    @Scheduled(fixedRate = 1000)
//    public void tierSchedule() throws IOException, ParseException {
//        List<StudyRule> all = studyRuleService.getAll();
//        for (StudyRule studyRule : all) {
//            log.info("티어별 실행");
//            Integer solvedCount = solvedApiService.getSolvedCount(studyRule.getRule().getDifficulty().toUpperCase());
//            // solvedCount 값을 저장해 줄 곳 필요
////            studyRule.getGoal().getMyStudy().setGoal(solvedCount);
//            //
//            System.out.println(solvedCount);
//        }
//    }

    @Scheduled(fixedRate = 1000)
    public void checkStudyRule() throws IOException, ParseException {
        log.info("스케줄러 실행");
        RsData<List<Member>> memberList = memberService.getAll();
        for (Member member : memberList.getData()) {

            log.info("tier");
            BaekJoon baekJoon = solvedApiService.getSolvedCount(member, 1, 6).getData();
            //goal 에 저장
            memberService.solve(member.getId(), baekJoon);
            //
            BaekJoon baekJoon1 = solvedApiService.getSolvedCount(member, 6, 11).getData();
            memberService.solve(member.getId(), baekJoon1);

            BaekJoon baekJoon2 = solvedApiService.getSolvedCount(member, 11, 16).getData();
            memberService.solve(member.getId(), baekJoon2);

            BaekJoon baekJoon3 = solvedApiService.getSolvedCount(member, 16, 21).getData();
            memberService.solve(member.getId(), baekJoon3);

            BaekJoon baekJoon4 = solvedApiService.getSolvedCount(member, 21, 26).getData();
            memberService.solve(member.getId(), baekJoon4);

            BaekJoon baekJoon5 = solvedApiService.getSolvedCount(member, 26, 31).getData();
            memberService.solve(member.getId(), baekJoon5);

        }
    }
}
