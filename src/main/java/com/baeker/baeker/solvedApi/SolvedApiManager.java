package com.baeker.baeker.solvedApi;

import com.baeker.baeker.member.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Getter
@RequiredArgsConstructor
public class SolvedApiManager {

    private final String BASE_URL = "https://solved.ac/api/v3/user";
    private final String api_user = "/show";
    private final String api_problem = "/problem_stats";
    private final String api_handle = "?handle=";


    //== 요청 정보 == //
    private String getUserInformation(String baekJoonName) throws UnsupportedEncodingException {
        return BASE_URL +
                api_user +
                api_handle +
                baekJoonName;

    }

    private String getProblemStats(String baekJoonName) throws UnsupportedEncodingException{
        return BASE_URL +
                api_problem +
                api_handle +
                baekJoonName;
    }



    /**
     * 문제풀이 로직
     */
    public JSONArray getProblemCount(String baekJoonName) throws IOException, ParseException {
        RestTemplate restTemplate = new RestTemplate();
        String jsonString = restTemplate.getForObject(getProblemStats(baekJoonName), String.class);
        JSONParser jsonParser = new JSONParser();
        Object jsonObject = jsonParser.parse(jsonString);

        return (JSONArray) jsonObject;
    }

    /**
     * 사용자 정보
     */
    public String findUser(String baekJoonName) throws IOException, ParseException {
        RestTemplate restTemplate = new RestTemplate();
        String jsonString = restTemplate.getForObject(getUserInformation(baekJoonName), String.class);

        JSONParser jsonParser = new JSONParser();
        Object jsonObject = jsonParser.parse(jsonString);

        JSONObject jsonBody = (JSONObject) jsonObject;

        return jsonBody.get("handle").toString();
    }
}