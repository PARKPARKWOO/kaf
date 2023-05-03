package com.baeker.baeker.solvedApi;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.embed.BaekJoonDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class ApiScheduler {

    private final SolvedApiService solvedApiService;

    private final MemberService memberService;


    /**
     * 티어 별 check
     */

    @Scheduled(fixedRate = 1000)
    public void checkStudyRule() throws IOException, ParseException {
        log.info("스케줄러 실행");
        RsData<List<Member>> memberList = memberService.getAll();
        for (Member member : memberList.getData()) {


            solvedApiService.getSolvedCount(member, 1, 6);

            Integer Silver = solvedApiService.getSolvedCount(member, 6, 11);

            Integer Gold = solvedApiService.getSolvedCount(member, 11, 16);

            Integer Platinum = solvedApiService.getSolvedCount(member, 16, 21);

            Integer Diamond = solvedApiService.getSolvedCount(member, 21, 26);

            Integer Ruby = solvedApiService.getSolvedCount(member, 26, 31);


//            BaekJoonDto baekJoon = BaekJoonDto.builder().bronze(Bronze).sliver(Silver).gold(Gold).platinum(Platinum)
//                    .diamond(Diamond).ruby(Ruby).build();
//
//            memberService.solve(member.getId(), baekJoon);
        }
    }
}
