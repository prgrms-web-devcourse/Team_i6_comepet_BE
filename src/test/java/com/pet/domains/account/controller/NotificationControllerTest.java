package com.pet.domains.account.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.pet.common.jwt.JwtMockToken;
import com.pet.domains.account.WithAccount;
import com.pet.domains.account.dto.request.NotificationUpdateParam;
import com.pet.domains.docs.BaseDocumentationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("알림 컨트롤러 테스트")
class NotificationControllerTest extends BaseDocumentationTest {

    @Test
    @DisplayName("알림 조회 요청 테스트")
    void getNotificationTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/notices")
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN)
            .accept(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-notifications",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.notifications").type(ARRAY).description("알림"),
                    fieldWithPath("data.notifications[0].nickname").type(STRING).description("유저 닉네임"),
                    fieldWithPath("data.notifications[0].image").type(STRING).description("유저 프로필 사진"),
                    fieldWithPath("data.notifications[0].postId").type(NUMBER).description("실종/보호 게시물 id"),
                    fieldWithPath("data.notifications[0].status").type(STRING).description("<<status,게시물 상태>>"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")
                ))
            );
    }

    @Test
    @WithAccount
    @DisplayName("알림 삭제 요청 테스트")
    void deleteNotificationTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/notices/{noticeId}", 1L)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken()));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(document("delete-notification",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()))
            );
    }

    @Test
    @DisplayName("알림 상태 변경 요청 테스트")
    void checkedNotificationTest() throws Exception {
        // given
        NotificationUpdateParam param = new NotificationUpdateParam(true);
        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/notices/{noticeId}", 1L)
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(param)));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(document("update-notification",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestFields(
                    fieldWithPath("checked").type(BOOLEAN).description("읽음 여부")
                ))
            );
    }

}
