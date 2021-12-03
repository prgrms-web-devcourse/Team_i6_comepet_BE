package com.pet.domains.area.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(CityController.class)
@AutoConfigureRestDocs
@DisplayName("시도/시군구 컨트롤러 테스트")
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("시도/시군구 조회 성공 테스트")
    void getCitiesTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/cities"));
        // then
        resultActions
            .andDo(print())
            .andDo(document("get-cities",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.cities").type(ARRAY).description("시도"),
                    fieldWithPath("data.cities[0].id").type(NUMBER).description("시도 id"),
                    fieldWithPath("data.cities[0].name").type(STRING).description("시도 이름"),
                    fieldWithPath("data.cities[0].towns").type(ARRAY).description("시도 id"),
                    fieldWithPath("data.cities[0].towns[0].id").type(NUMBER).description("시도 id"),
                    fieldWithPath("data.cities[0].towns[0].name").type(STRING).description("시도 id"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );
    }

}
