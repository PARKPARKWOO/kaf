package com.baeker.baeker.studyRule.solvedApi;

import com.baeker.baeker.base.request.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SolvedApiController {
    private final SolvedApiService solvedApiService;

    private final Rq rq;



}
