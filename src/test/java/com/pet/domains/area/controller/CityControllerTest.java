package com.pet.domains.area.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CityController.class)
@DisplayName("시도/시군구 컨트롤러 테스트")
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("시도/시군구 조회 성공 테스트")
    void getCities() throws Exception {
        mockMvc.perform(get("/api/v1/cities"))
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                jsonPath("data").exists(),
                jsonPath("$.data.cities").isArray(),
                jsonPath("$.data.cities[0].id").isNumber(),
                jsonPath("$.data.cities[0].name").isString(),
                jsonPath("$.data.cities[0].towns").isArray(),
                jsonPath("$.data.cities[0].towns[0].id").isNumber(),
                jsonPath("$.data.cities[0].towns[0].name").isString(),
                jsonPath("$.serverDateTime").isString(),
                content().contentType(MediaType.APPLICATION_JSON)
            )
        ;
    }
}