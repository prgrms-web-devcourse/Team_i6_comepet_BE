package com.pet.domains.account.controller;

import com.pet.common.jwt.JwtMockToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = NotificationController.class)
@AutoConfigureRestDocs
@DisplayName("알림 컨트롤러 테스트")
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("알림 조회 요청 테스트")
    void getNotification() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/notices")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-notification",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.notifications").type(ARRAY).description("알림"),
                    fieldWithPath("data.notifications[0].nickname").type(STRING).description("유저 닉네임"),
                    fieldWithPath("data.notifications[0].image").type(STRING).description("유저 프로필 사진"),
                    fieldWithPath("data.notifications[0].postId").type(NUMBER).description("실종/보호 게시물 id"),
                    fieldWithPath("data.notifications[0].status").type(STRING).description("실종/보호 게시물 상태"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")
                ))
            );
    }


}