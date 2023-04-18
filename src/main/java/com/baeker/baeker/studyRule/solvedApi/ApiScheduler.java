package com.baeker.baeker.studyRule.solvedApi;

import com.baeker.baeker.base.request.Rq;
import com.baeker.baeker.member.Member;
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


    /**
     * 티어 체크 후 Study
     */
    @Scheduled(fixedRate = 100000)
    public void tierSchedule() throws IOException, ParseException {
        List<StudyRule> all = studyRuleService.getAll();
        for (StudyRule studyRule : all) {
            log.info("티어별 실행");
            Integer solvedCount = solvedApiService.getTier(studyRule.getRule().getDifficulty().toUpperCase());
            // solvedCount 값을 저장해 줄 곳 필요

            //
            System.out.println(solvedCount);
        }
    }

    @Scheduled(fixedRate = 100000)
    public void solvedCountSchedule() throws IOException, ParseException {
        log.info("문제풀이수 스케줄러");
        Long solvedCount = solvedApiService.getSolvedCount();
        System.out.println(solvedCount);
    }
}
