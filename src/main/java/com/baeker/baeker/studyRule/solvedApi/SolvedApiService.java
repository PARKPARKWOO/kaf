package com.baeker.baeker.studyRule.solvedApi;

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



    //== 티어확인 및 문제풀이 리턴==//
    public Integer getTier(String tier) throws IOException, ParseException, UnsupportedEncodingException {
        JSONArray test = this.solvedApiManager.getProblemCount();
        Integer temp = 0;
        if (test.size() > 0) {
            for (Object o : test) {
                JSONObject jsonObj = (JSONObject) o;
                switch (tier) {
                    case "BRONZE" -> {
                        if (jsonObj.get("level").equals(1L) || jsonObj.get("level").equals(2L) || jsonObj.get("level").equals(3L) || jsonObj.get("level").equals(4L) || jsonObj.get("level").equals(5L)) {
                            temp += (Integer) jsonObj.get("solved");
                        }
                    }
                    case "SILVER" -> {
                        if (jsonObj.get("level").equals(6L) || jsonObj.get("level").equals(7L) || jsonObj.get("level").equals(8L) || jsonObj.get("level").equals(9L) || jsonObj.get("level").equals(10L)) {
                            temp += (Integer) jsonObj.get("solved");
                        }
                    }
                    case "GOLD" -> {
                        if (jsonObj.get("level").equals(11L) || jsonObj.get("level").equals(12L) || jsonObj.get("level").equals(13L) || jsonObj.get("level").equals(14L) || jsonObj.get("level").equals(15L)) {
                            temp += (Integer) jsonObj.get("solved");
                        }
                    }
                    case "PLATINUM" -> {
                        if (jsonObj.get("level").equals(16L) || jsonObj.get("level").equals(17L) || jsonObj.get("level").equals(18L) || jsonObj.get("level").equals(19L) || jsonObj.get("level").equals(20L)) {
                            temp += (Integer) jsonObj.get("solved");
                        }
                    }
                    case "DIAMOND" -> {
                        if (jsonObj.get("level").equals(21L) || jsonObj.get("level").equals(22L) || jsonObj.get("level").equals(23L) || jsonObj.get("level").equals(24L) || jsonObj.get("level").equals(25L)) {
                            temp += (Integer) jsonObj.get("solved");
                        }
                    }
                    case "RUBY" -> {
                        if (jsonObj.get("level").equals(26L) || jsonObj.get("level").equals(27L) || jsonObj.get("level").equals(28L) || jsonObj.get("level").equals(29L) || jsonObj.get("level").equals(30L)) {
                            temp += (Integer) jsonObj.get("solved");
                        }
                    }
                }
            }
        }
        return temp;
    }

    public Long getSolvedCount() throws IOException, ParseException, UnsupportedEncodingException{
        return Long.parseLong(this.solvedApiManager.getSolvedCount());
    }
}
