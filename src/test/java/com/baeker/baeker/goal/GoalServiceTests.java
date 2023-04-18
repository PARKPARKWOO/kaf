package com.baeker.baeker.goal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GoalServiceTests {
    @Autowired
    private GoalService goalService;
    @Autowired
    private GoalRepository goalRepository;


    @Test
    @DisplayName("CRUD")
    void crudTests() {

    }
}
