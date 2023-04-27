package com.baeker.baeker.base.messge;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
public class LineNotifyService {


    private final String group_token = "Bearer KHQaRNXzB3zXBpspqnYs8aIo9HtWHpChWirFTC84hKb";
    private final String my_token = "Bearer b5qtToJM5kJoMGe7121HYT9M9JH6ZJSb1uvgCurYcej";
    private final static String url = "https://notify-api.line.me/api/notify?message=";
    private final static String HEADER_AUTH = "Authorization";

    public void alarmToMe(String msg) {
        alarm(my_token, msg);
    }

    public void alarmToGroup(String msg) {
        alarm(group_token, msg);
    }

    private void alarm(String groupToken, String msg) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HEADER_AUTH, groupToken);
        String apiContext = url + msg;

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        restTemplate.exchange(apiContext, HttpMethod.POST, httpEntity, String.class);
    }

}
