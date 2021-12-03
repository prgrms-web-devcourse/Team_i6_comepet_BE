package com.pet.domains.account.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.common.config.JwtMockToken;
import com.pet.domains.account.dto.request.AccountCreateParam;
import com.pet.domains.account.dto.request.AccountLonginParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(
    value = AccountController.class,
    includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtMockToken.class)
)
@DisplayName("회원 테스트")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 가입 요청 성공 테스트")
    void signUpTest() throws Exception {
        // given
        AccountCreateParam param = new AccountCreateParam("tester", "tester@email.com", "12345678a!");
        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(param)));

        // then
        resultActions
            .andDo(print())
            .andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("data").exists(),
                jsonPath("$.data.id").isNumber(),
                jsonPath("$.data.token").isString(),
                jsonPath("$.serverDateTime").isString()
            );
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginTest() throws Exception {
        // given
        AccountLonginParam param = new AccountLonginParam("tester@email.com", "12345678a!");
        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(param)));

        // then
        resultActions
            .andDo(print())
            .andExpectAll(
                status().isCreated(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("data").exists(),
                jsonPath("$.data.id").isNumber(),
                jsonPath("$.data.token").isString(),
                jsonPath("$.serverDateTime").isString()
            );
    }

}
