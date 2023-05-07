package com.baeker.baeker.solvedApi;


import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.embed.BaekJoonDto;
import com.baeker.baeker.member.form.MemberJoinForm;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
public class ApiSchedulerTests {
    @SpyBean
    private ApiScheduler apiScheduler;


    @Test
    @DisplayName("스케줄러 테스트")
    void solvedCountSchedulerTest() throws IOException, ParseException {
        await().atMost(4, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(apiScheduler, atLeast(2)).checkStudyRule();
        });
    }


}
