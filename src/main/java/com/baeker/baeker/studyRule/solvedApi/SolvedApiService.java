package com.baeker.baeker.studyRule.solvedApi;

import com.baeker.baeker.base.request.RsData;
import com.baeker.baeker.member.Member;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.member.embed.BaekJoon;
import com.baeker.baeker.myStudy.MyStudy;
import com.baeker.baeker.member.MemberService;
import com.baeker.baeker.rule.Rule;
import com.baeker.baeker.rule.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
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

    public RsData<BaekJoon> getSolvedCount(Member member, Integer min, Integer max) throws IOException, ParseException {
        JSONArray test = this.solvedApiManager.getProblemCount(member);
        Integer solvedCount = 0;
        for (Object o : test) {
            JSONObject jsonObject = (JSONObject) o;
            if ((int)jsonObject.get("level") >= min && (int) jsonObject.get("level") < max) {
                solvedCount += (Integer) jsonObject.get("solved");
            }
        }

        return RsData.of("S-1", "SolvedCount", member.getBaekJoon());
    }


    //== 티어확인 및 문제풀이 리턴==//
//    public Integer getSolvedCount(String tier, MyStudy myStudy) throws IOException, ParseException, UnsupportedEncodingException {
//        JSONArray test = this.solvedApiManager.getProblemCount(myStudy);
//        Integer temp = 0;
//        if (test.size() > 0) {
//            for (Object o : test) {
//                JSONObject jsonObj = (JSONObject) o;
//                switch (tier) {
//                    case "BRONZE" -> {
//                        if (jsonObj.get("level").equals(1L) || jsonObj.get("level").equals(2L) || jsonObj.get("level").equals(3L) || jsonObj.get("level").equals(4L) || jsonObj.get("level").equals(5L)) {
//                            temp += (Integer) jsonObj.get("solved");
//                        }
//                    }
//                    case "SILVER" -> {
//                        if (jsonObj.get("level").equals(6L) || jsonObj.get("level").equals(7L) || jsonObj.get("level").equals(8L) || jsonObj.get("level").equals(9L) || jsonObj.get("level").equals(10L)) {
//                            temp += (Integer) jsonObj.get("solved");
//                        }
//                    }
//                    case "GOLD" -> {
//                        if (jsonObj.get("level").equals(11L) || jsonObj.get("level").equals(12L) || jsonObj.get("level").equals(13L) || jsonObj.get("level").equals(14L) || jsonObj.get("level").equals(15L)) {
//                            temp += (Integer) jsonObj.get("solved");
//                        }
//                    }
//                    case "PLATINUM" -> {
//                        if (jsonObj.get("level").equals(16L) || jsonObj.get("level").equals(17L) || jsonObj.get("level").equals(18L) || jsonObj.get("level").equals(19L) || jsonObj.get("level").equals(20L)) {
//                            temp += (Integer) jsonObj.get("solved");
//                        }
//                    }
//                    case "DIAMOND" -> {
//                        if (jsonObj.get("level").equals(21L) || jsonObj.get("level").equals(22L) || jsonObj.get("level").equals(23L) || jsonObj.get("level").equals(24L) || jsonObj.get("level").equals(25L)) {
//                            temp += (Integer) jsonObj.get("solved");
//                        }
//                    }
//                    case "RUBY" -> {
//                        if (jsonObj.get("level").equals(26L) || jsonObj.get("level").equals(27L) || jsonObj.get("level").equals(28L) || jsonObj.get("level").equals(29L) || jsonObj.get("level").equals(30L)) {
//                            temp += (Integer) jsonObj.get("solved");
//                        }
//                    }
//                }
//            }
//        }
//        return temp;
//    }

    public Integer getSolvedCount(Member member) throws IOException, ParseException, UnsupportedEncodingException {
            return Integer.parseInt(this.solvedApiManager.getSolvedCount(member));
        }
}
