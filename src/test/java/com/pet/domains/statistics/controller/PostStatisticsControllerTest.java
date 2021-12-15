package com.pet.domains.statistics.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.pet.domains.docs.BaseDocumentationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("통계 컨트롤러 테스트")
class PostStatisticsControllerTest extends BaseDocumentationTest {

    @Test
    @DisplayName("게시글 통계 데이터 조회 테스트")
    void getPostStatisticsTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/statistics")
            .accept(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-post-statistics",
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
                    fieldWithPath("data.missing").type(NUMBER).description("실종 게시글 수"),
                    fieldWithPath("data.detection").type(NUMBER).description("목격 게시글 수"),
                    fieldWithPath("data.protection").type(NUMBER).description("보호 게시글 수"),
                    fieldWithPath("data.completion").type(NUMBER).description("완료 게시글 수"),
                    fieldWithPath("data.date").type(STRING).description("통계 시간"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );
    }
}
