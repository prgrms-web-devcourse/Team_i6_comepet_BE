package com.pet.domains.comment.controller;

import static com.pet.domains.docs.utils.ApiDocumentUtils.getDocumentRequest;
import static com.pet.domains.docs.utils.ApiDocumentUtils.getDocumentResponse;
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
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
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
import com.pet.domains.comment.dto.response.CommentWriteResult;
import com.pet.domains.docs.BaseDocumentationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("?????? ???????????? ?????????")
class CommentControllerTest extends BaseDocumentationTest {

    @Test
    @WithAccount
    @DisplayName("?????? ?????? ?????????")
    void createCommentTest() throws Exception {
        // given
        CommentCreateParam createParam = CommentCreateParam.builder()
            .postId(1L)
            .content("content")
            .parentCommentId(13L)
            .build();
        CommentWriteResult createResult = new CommentWriteResult(
            123L,
            createParam.getContent(),
            LocalDateTime.now(),
            new CommentWriteResult.Account(12L, "??????", "http://../.jpg"),
            List.of(new CommentWriteResult.ChildComment(
                13L,
                "?????? ??????",
                LocalDateTime.now(),
                new CommentWriteResult.Account(154L, "????????? ??????", "http://../.jpg"))
            ),
            false
        );
        given(commentService.createComment(any(Account.class), any(CommentCreateParam.class))).willReturn(createResult);

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
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestFields(
                    fieldWithPath("postId").description("?????? ????????? ?????????"),
                    fieldWithPath("content").description("?????? ??????"),
                    fieldWithPath("parentCommentId").description("?????? ?????? ?????????").optional()
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.id").type(NUMBER).description("?????? ?????????"),
                    fieldWithPath("data.content").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.createdAt").type(STRING).description("?????? ????????????"),
                    fieldWithPath("data.deleted").type(BOOLEAN).description("????????? ?????? ??????"),
                    fieldWithPath("data.account").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.account.id").type(NUMBER).description("????????? ?????????"),
                    fieldWithPath("data.account.nickname").type(STRING).description("????????? ?????????"),
                    fieldWithPath("data.account.image").type(STRING).description("????????? ????????? ??????"),
                    fieldWithPath("data.childComments").type(ARRAY).description("????????? ????????? ??????"),
                    fieldWithPath("data.childComments[].id").type(NUMBER).description("????????? ?????????"),
                    fieldWithPath("data.childComments[].content").type(STRING).description("????????? ??????"),
                    fieldWithPath("data.childComments[].createdAt").type(STRING).description("????????? ????????????"),
                    fieldWithPath("data.childComments[].account").type(OBJECT).description("????????? ?????????"),
                    fieldWithPath("data.childComments[].account.id").type(NUMBER).description("????????? ?????????"),
                    fieldWithPath("data.childComments[].account.nickname").type(STRING)
                        .description("????????? ?????????"),
                    fieldWithPath("data.childComments[].account.image").type(STRING)
                        .description("????????? ????????? ??????"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")))
            );
    }

    @Test
    @WithAccount
    @DisplayName("?????? ?????? ?????????")
    void updateCommentTest() throws Exception {
        // given
        CommentUpdateParam updateParam = CommentUpdateParam.builder()
            .content("updated content")
            .build();
        CommentWriteResult updateResult = new CommentWriteResult(
            123L,
            updateParam.getContent(),
            LocalDateTime.now(),
            new CommentWriteResult.Account(12L, "??????", "http://../.jpg"),
            List.of(new CommentWriteResult.ChildComment(
                13L,
                "?????? ??????",
                LocalDateTime.now(),
                new CommentWriteResult.Account(154L, "????????? ??????", "http://../.jpg"))
            ),
            false
        );
        given(commentService.updateComment(anyLong(), any(CommentUpdateParam.class), any(Account.class)))
            .willReturn(updateResult);

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
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                    parameterWithName("commentId").description("?????? ?????????")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestFields(
                    fieldWithPath("content").description("????????? ?????? ??????")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.id").type(NUMBER).description("?????? ?????????"),
                    fieldWithPath("data.content").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.createdAt").type(STRING).description("?????? ????????????"),
                    fieldWithPath("data.deleted").type(BOOLEAN).description("????????? ?????? ??????"),
                    fieldWithPath("data.account").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.account.id").type(NUMBER).description("????????? ?????????"),
                    fieldWithPath("data.account.nickname").type(STRING).description("????????? ?????????"),
                    fieldWithPath("data.account.image").type(STRING).description("????????? ????????? ??????"),
                    fieldWithPath("data.childComments").type(ARRAY).description("????????? ????????? ??????"),
                    fieldWithPath("data.childComments[].id").type(NUMBER).description("????????? ?????????"),
                    fieldWithPath("data.childComments[].content").type(STRING).description("????????? ??????"),
                    fieldWithPath("data.childComments[].createdAt").type(STRING).description("????????? ????????????"),
                    fieldWithPath("data.childComments[].account").type(OBJECT).description("????????? ?????????"),
                    fieldWithPath("data.childComments[].account.id").type(NUMBER).description("????????? ?????????"),
                    fieldWithPath("data.childComments[].account.nickname").type(STRING)
                        .description("????????? ?????????"),
                    fieldWithPath("data.childComments[].account.image").type(STRING)
                        .description("????????? ????????? ??????"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")))
            );
    }

    @Test
    @WithAccount
    @DisplayName("?????? ?????? ?????????")
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
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                    parameterWithName("commentId").description("?????? ?????????")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ))
            );
    }

}
