package com.baeker.baeker.base.messge.kakao;

import com.baeker.baeker.base.messge.kakao.dto.KakaoFriendListDto;
import com.baeker.baeker.base.messge.kakao.service.CustomMessageService;
import com.baeker.baeker.base.messge.kakao.service.KakaoMessageService;
import com.baeker.baeker.base.request.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoMessageController {

    private final CustomMessageService msgService;
    private final KakaoMessageService kakaoService;
    private final Rq rq;

    //-- 나에게 메시지 보내기 --//
    @GetMapping("/me")
    public String sendMessage() {
        String accessToken = rq.getMember().getAccessToken();

        boolean result = msgService.sendMessage(accessToken);

        if (result)
            return "메시지 전송 완료";
        else
            return "메시지 전송 실패";
    }

    //-- 내 친구 목록 가져오기 --//
    @GetMapping("/list")
    public List<KakaoFriendListDto> getList() {
        String accessToken = rq.getMember().getAccessToken();

        return kakaoService.getFriendsList(accessToken);
    }
}
