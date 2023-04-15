package com.baeker.baeker.studyRule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class StudyRuleServiceTests {

    @Autowired
    private StudyRuleRepository studyRuleRepository;

    @Autowired
    private StudyRuleService studyRuleService;


    @Test
    void dummytests() {

    }
}
