package com.baeker.baeker.solvedApi;

import com.baeker.baeker.base.event.BaekJoonEvent;
import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.embed.BaekJoonDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import com.baeker.baeker.member.snapshot.MemberSnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import com.baeker.baeker.member.snapshot.MemberSnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import com.baeker.baeker.member.snapshot.MemberSnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import com.baeker.baeker.member.snapshot.MemberSnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class ApiScheduler {

    private final SolvedApiService solvedApiService;

    private final MemberService memberService;

    private final ApplicationEventPublisher publisher;

    /**
     * 티어 별 check
     */


    @Scheduled(cron = "${scheduler.cron.value}")
    public void checkStudyRule() throws IOException, ParseException {
        log.info("스케줄러 실행");
        RsData<List<Member>> memberList = memberService.getAll();
        for (Member member : memberList.getData()) {
            int Bronze = solvedApiService.getSolvedCount(member, 1, 6) - member.getBronze();

            int Silver = solvedApiService.getSolvedCount(member, 6, 11) - member.getSliver();

            int Gold = solvedApiService.getSolvedCount(member, 11, 16) - member.getGold();

            int Platinum = solvedApiService.getSolvedCount(member, 16, 21) - member.getPlatinum();

            int Diamond = solvedApiService.getSolvedCount(member, 21, 26) - member.getDiamond();

            int Ruby = solvedApiService.getSolvedCount(member, 26, 31) - member.getRuby();

            BaekJoonDto dto = new BaekJoonDto(Bronze, Silver, Gold, Platinum, Diamond, Ruby);
            publisher.publishEvent(new BaekJoonEvent(this, member, dto));

        }
    }
}
