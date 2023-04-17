package com.baeker.baeker.rule;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class RuleControllerTests {

    @Autowired
    private RuleController ruleController;


    @Test
    @DisplayName(value = "컨트롤러")
    void dummy() {

    }
}
