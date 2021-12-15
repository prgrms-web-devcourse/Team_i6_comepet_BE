package com.pet.domains.area.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.pet.common.response.ApiResponse;
import com.pet.domains.area.dto.response.CityReadResults;
import com.pet.domains.docs.BaseDocumentationTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("시도/시군구 컨트롤러 테스트")
class CityControllerTest extends BaseDocumentationTest {

    @Test
    @DisplayName("시도/시군구 조회 성공 테스트")
    void getCitiesTest() throws Exception {
        // given
        given(cityService.getAllTownAndCity()).willReturn(
            CityReadResults.of(
                List.of(
                    CityReadResults.City.of(1L, "서울특별시", List.of(
                        CityReadResults.City.Town.of(1L, "강남구"),
                        CityReadResults.City.Town.of(2L, "강동구"),
                        CityReadResults.City.Town.of(3L, "강서구"),
                        CityReadResults.City.Town.of(4L, "관악구"),
                        CityReadResults.City.Town.of(5L, "광진구")
                    )),
                    CityReadResults.City.of(2L, "부산광역시", List.of(
                        CityReadResults.City.Town.of(22L, "중구"),
                        CityReadResults.City.Town.of(23L, "서구"),
                        CityReadResults.City.Town.of(24L, "동구"),
                        CityReadResults.City.Town.of(25L, "영도구")
                    )),
                    CityReadResults.City.of(3L, "대구광역시", List.of(
                        CityReadResults.City.Town.of(37L, "북구"),
                        CityReadResults.City.Town.of(38L, "동구"),
                        CityReadResults.City.Town.of(40L, "중구")

                    ))
                )
            ));
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/cities")
            .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-cities",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),
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
