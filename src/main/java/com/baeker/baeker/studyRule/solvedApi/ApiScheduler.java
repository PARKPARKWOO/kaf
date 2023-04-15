package com.baeker.baeker.studyRule.solvedApi;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class ApiScheduler {
    @Autowired
    private SolvedApiManager solvedApiManager;

    @Autowired
    private SolvedApiService solvedApiService;

    @Scheduled(fixedRate = 1000)
    public void tierSchedule() throws IOException, ParseException {
        log.info("티어별 실행");
        Long solvedCount = solvedApiService.getTier("Gold");
        System.out.println(solvedCount);
    }

    @Scheduled(fixedRate = 1000)
    public void solvedCountSchedule() throws IOException, ParseException {
        log.info("문제풀이수 스케줄러");
        Long solvedCount = solvedApiService.getSolvedCount();
        System.out.println(solvedCount);
    }
}
