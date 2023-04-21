package com.baeker.baeker.myStudy;

import com.baeker.baeker.base.request.Rq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/my_study")
@RequiredArgsConstructor
public class MyStudyController {

    private final MyStudyService myStudyService;
    private final Rq rq;

    //-- 가입 신청하기 --//

    //-- 스터디로 초대하기 --//
}
