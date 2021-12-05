package com.pet.domains.post.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

@WebMvcTest(MissingPostsController.class)
@AutoConfigureRestDocs
@DisplayName("실종 게시글 컨트롤러 docs 테스트")
class MissingPostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("특정 실종게시글의 댓글 리스트 조회 테스트")
    void getShelterPostsTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/missing-posts/{postId}/comments", 1L)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(document("get-missing-posts-comments",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("postId").description("실종 게시글 아이디")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.comments").type(ARRAY).description("댓글 목록"),
                    fieldWithPath("data.comments[].id").type(NUMBER).description("댓글 아이디"),
                    fieldWithPath("data.comments[].content").type(STRING).description("댓글 내용"),
                    fieldWithPath("data.comments[].createdAt").type(STRING).description("댓글 작성날짜"),
                    fieldWithPath("data.comments[].user").type(OBJECT).description("댓글 작성자"),
                    fieldWithPath("data.comments[].user.id").type(NUMBER).description("작성자 아이디"),
                    fieldWithPath("data.comments[].user.nickname").type(STRING).description("작성자 닉네임"),
                    fieldWithPath("data.comments[].user.image").type(STRING).description("작성자 프로필 사진"),
                    fieldWithPath("data.totalElements").type(NUMBER).description("전체 결과 수"),
                    fieldWithPath("data.last").type(BOOLEAN).description("마지막 페이지 여부"),
                    fieldWithPath("data.size").type(NUMBER).description("페이지당 요청 수"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );
    }

}
