package com.pet.domains.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.pet.domains.account.WithAccount;
import com.pet.domains.account.domain.Account;
import com.pet.domains.comment.dto.request.CommentCreateParam;
import com.pet.domains.comment.dto.request.CommentUpdateParam;
import com.pet.domains.docs.BaseDocumentationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("댓글 컨트롤러 테스트")
class CommentControllerTest extends BaseDocumentationTest {

    @Test
    @WithAccount
    @DisplayName("댓글 생성 테스트")
    void createCommentTest() throws Exception {
        // given
        given(commentService.createComment(any(Account.class), any(CommentCreateParam.class))).willReturn(1L);
        CommentCreateParam createParam = CommentCreateParam.builder()
            .postId(1L)
            .content("content")
            .parentCommentId(13L)
            .build();

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/comments")
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(createParam)));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("create-comment",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestFields(
                    fieldWithPath("postId").description("실종 게시글 아이디"),
                    fieldWithPath("content").description("댓글 내용"),
                    fieldWithPath("parentCommentId").description("부모 댓글 아이디").optional()
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.id").type(NUMBER).description("댓글 아이디"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );
    }

    @Test
    @WithAccount
    @DisplayName("댓글 수정 테스트")
    void updateCommentTest() throws Exception {
        // given
        given(commentService.updateComment(anyLong(), any(CommentUpdateParam.class), any(Account.class)))
            .willReturn(1L);
        CommentUpdateParam updateParam = CommentUpdateParam.builder()
            .content("updated content")
            .build();

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/comments/{commentId}", 1L)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(updateParam)));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("update-comment",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("commentId").description("댓글 아이디")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestFields(
                    fieldWithPath("content").description("수정할 댓글 내용")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.id").type(NUMBER).description("댓글 아이디"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );
    }

    @Test
    @WithAccount
    @DisplayName("댓글 삭제 테스트")
    void deleteCommentTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/comments/{commentId}", 1L)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken()));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(document("delete-comment",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("commentId").description("댓글 아이디")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ))
            );
    }

}
