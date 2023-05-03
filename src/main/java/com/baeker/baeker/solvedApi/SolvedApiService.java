package com.baeker.baeker.solvedApi;

import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolvedApiService {

    private final SolvedApiManager solvedApiManager;
    private final MemberService memberService;

    /**
     * 난이도별 체크 후 문제풀이 수 리턴
     */
    public Integer getSolvedCount(Member member, Integer min, Integer max) throws IOException, ParseException {
        JSONArray test = this.solvedApiManager.getProblemCount(member);
        Integer solvedCount = 0;
        for (Object o : test) {
            JSONObject jsonObject = (JSONObject) o;
            if ((int)jsonObject.get("level") >= min && (int) jsonObject.get("level") < max) {
                solvedCount += (Integer) jsonObject.get("solved");
            }
        }

        return solvedCount;
    }
    

    public Integer getSolvedCount(Member member) throws IOException, ParseException, UnsupportedEncodingException {
            return Integer.parseInt(this.solvedApiManager.getSolvedCount(member));
        }
}
