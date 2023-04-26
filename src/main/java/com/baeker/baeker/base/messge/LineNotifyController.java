package com.baeker.baeker.base.messge;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/line")
@RequiredArgsConstructor
public class LineNotifyController {

    private final LineNotifyService service;

    @GetMapping("/test")
    public String callMsg() {
        service.alarmToMe("나에게 전송한 메시지");
//        service.alarmToGroup("그룹에게 전송한 메시지");

        return "redirect:/";
    }
}
