package com.baeker.baeker.base.messge.kakao;

import com.baeker.baeker.base.request.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoMessageController {

    private final CustomMessageService msgService;
    private final Rq rq;

    @GetMapping("/kakao")
    public String sendMessage() {
        String accessToken = rq.getMember().getAccessToken();

        msgService.sendMessage(accessToken);
        return "메시지 전송 완료";
    }
}
