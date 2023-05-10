package com.baeker.baeker.solvedApi;

import static org.assertj.core.api.Assertions.assertThat;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.embed.BaekJoonDto;
import com.baeker.baeker.member.form.MemberJoinForm;
import com.baeker.baeker.member.snapshot.MemberSnapshot;
import com.baeker.baeker.member.snapshot.MemberSnapshotRepository;
import com.baeker.baeker.study.Study;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@Transactional
public class SolvedApiServiceTests {
    @Autowired
    private SolvedApiService solvedApiService;
    @Autowired
    private MemberService memberService;

    @Autowired private MemberSnapshotRepository memberSnapshotRepository;

    private Member create(String username, String name) {

        MemberJoinForm form = new MemberJoinForm(username, name, "", "1234", "1234", "");
        return memberService.join(form).getData();
    }

    private void connect(Member member, String baekJoonName) {
        BaekJoonDto dummy = new BaekJoonDto();
        RsData<Member> memberRsData = memberService.connectBaekJoon(member, baekJoonName);
    }

    @Test
    @DisplayName("아이디 유무 확인")
    void findUserTests() throws IOException, ParseException {
        assertThat(solvedApiService.findUser("wy9295")).isTrue();
        assertThat(solvedApiService.findUser("1")).isFalse();
    }

    @Test
    @DisplayName("백준 수동 update")
    void getSolvedCountTest() throws IOException, ParseException {
        Member member = create("user1", "member1");
        connect(member, "wy9295");
        solvedApiService.getSolvedCount(member.getId());
    }

    @Test
    @DisplayName("문제풀이 수 난이도 별 리턴")
    void getSolvedCountTests() throws IOException, ParseException {
        Member member = create("user1", "member1");
        connect(member, "wy9295");
        Integer solvedCount = solvedApiService.getSolvedCount(member.getId(), 1, 6);
        System.out.println("wy9295 :" + solvedCount);
    }



}
