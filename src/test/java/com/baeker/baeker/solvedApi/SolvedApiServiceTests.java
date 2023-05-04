package com.baeker.baeker.solvedApi;

import static org.assertj.core.api.Assertions.assertThat;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

@SpringBootTest
public class SolvedApiServiceTests {
    @Autowired
    private SolvedApiService solvedApiService;

    @Test
    @DisplayName("아이디 유무 확인")
    void findUserTests() throws IOException, ParseException {
        assertThat(solvedApiService.findUser("wy9295")).isTrue();
        assertThat(solvedApiService.findUser("1")).isFalse();
    }
}
