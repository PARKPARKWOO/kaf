package com.baeker.baeker.studyRule.solvedApi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class SolvedApiManager {

    private final String BASE_URL = "https://solved.ac/api/v3/user";
    private final String api_user = "/show";
    private final String api_problem = "/problem_stats";
    private final String api_handle = "?handle=";



    private String getUserInformation() throws UnsupportedEncodingException {
        return BASE_URL +
                api_user +
                api_handle + "wy9295";
    }

    private String getProblemStats() throws UnsupportedEncodingException{
        RestTemplate restTemplate = new RestTemplate();
        return BASE_URL +
                api_problem +
                api_handle + "wy9295";
    }


    //==문제풀이 로직==//
    public String  getSolvedCount() throws IOException, ParseException {
        RestTemplate restTemplate = new RestTemplate();
        String jsonString = restTemplate.getForObject(getUserInformation(), String.class);

        JSONParser jsonParser = new JSONParser();
        Object jsonObject = jsonParser.parse(jsonString);

        JSONObject jsonBody = (JSONObject) jsonObject;

        return jsonBody.get("solvedCount").toString();
    }

    public JSONArray getProblemCount() throws IOException, ParseException {
        RestTemplate restTemplate = new RestTemplate();
        String jsonString = restTemplate.getForObject(getProblemStats(), String.class);

        JSONParser jsonParser = new JSONParser();
        Object jsonObject = jsonParser.parse(jsonString);

        return (JSONArray) jsonObject;
    }

}