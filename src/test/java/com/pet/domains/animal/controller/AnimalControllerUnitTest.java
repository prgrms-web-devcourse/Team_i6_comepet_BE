package com.pet.domains.animal.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AnimalController.class)
@DisplayName("동물/품종 컨트롤러 테스트")
class AnimalControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("동물/품종 조회 성공 테스트")
    void getAnimalsTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/animals"))
            .andDo(print());

        // then
        resultActions.andExpectAll(
            status().isOk(),
            jsonPath("data").exists(),
            jsonPath("$.data.animals").isArray(),
            jsonPath("$.data.animals[0].id").isNumber(),
            jsonPath("$.data.animals[0].name").isString(),
            jsonPath("$.data.animals[0].kinds").isArray(),
            jsonPath("$.data.animals[0].kinds[0].id").isNumber(),
            jsonPath("$.data.animals[0].kinds[0].name").isString(),
            jsonPath("$.serverDateTime").isString(),
            content().contentType(MediaType.APPLICATION_JSON)
        );
    }
}
