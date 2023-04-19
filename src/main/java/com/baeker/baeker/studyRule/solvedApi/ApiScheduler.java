package com.baeker.baeker.studyRule.solvedApi;

import com.baeker.baeker.base.request.Rq;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

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
//
//    @Scheduled(fixedRate = 1000)
//    public void solvedCountSchedule() throws IOException, ParseException {
//        Integer solvedCount = solvedApiService.getSolvedCount();
//    }
//



//    @Scheduled(fixedRate = 1000)
//    public void checkStudyRule() throws IOException, ParseException {
//        log.info("스케줄러 실행");
//        List<StudyRule> studyRules = studyRuleService.getAll();
//        for (StudyRule studyRule : studyRules) {
//            for (MyStudy myStudy: studyRule.getStudy().getMyStudies()) {
//                if (studyRule.getRule().getDifficulty().equals("NONE")) {
//                    log.info("난이도 X");
//                    Integer solvedCount = solvedApiService.getSolvedCount(studyRule.getRule().getDifficulty(), myStudy);
//                    // goal 에 저장
//                    myStudy.toBuilder().solvedCount(solvedCount);
//                    //
//                } else {
//                    log.info("난이도 O");
//                    Integer solvedCount = solvedApiService.getSolvedCount(studyRule.getRule().getDifficulty().toUpperCase(),myStudy);
//                    //goal 에 저장
//                    myStudy.toBuilder().solvedCount(solvedCount);
//                    //
//                }
//            }
//        }
//    }
}
