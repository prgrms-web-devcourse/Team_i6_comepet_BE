package com.pet.domains.apitest;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.common.config.SecurityConfig;
import com.pet.common.jwt.JwtAuthentication;
import com.pet.common.property.JwtProperty;
import com.pet.domains.account.WithAccount;
import com.pet.domains.account.controller.AccountController;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.service.AccountService;
import com.pet.domains.area.controller.CityController;
import com.pet.domains.comment.dto.request.CommentCreateParam;
import com.pet.domains.comment.dto.request.CommentUpdateParam;
import com.pet.domains.comment.dto.response.CommentWriteResult;
import com.pet.domains.comment.service.CommentService;
import com.pet.domains.docs.BaseDocumentationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(value = CommentService.class,
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
    })
@AutoConfigureRestDocs
@EnableConfigurationProperties(value = JwtProperty.class)
@DisplayName("댓글 컨트롤러 테스트")
@Disabled
class CommentControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected AccountService accountService;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    protected JwtAuthentication getAuthenticationToken() {
        return (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // @Test
    // @WithAccount
    // @DisplayName("댓글 생성 테스트")
    // void createCommentTest() throws Exception {
    //     // given
    //     CommentCreateParam createParam = CommentCreateParam.builder()
    //         .postId(1L)
    //         .content("content")
    //         .parentCommentId(13L)
    //         .build();
    //     CommentWriteResult createResult = new CommentWriteResult(
    //         123L,
    //         createParam.getContent(),
    //         LocalDateTime.now(),
    //         new CommentWriteResult.Account(12L, "회원", "http://../.jpg"),
    //         List.of(new CommentWriteResult.ChildComment(
    //             13L,
    //             "자식 댓글",
    //             LocalDateTime.now(),
    //             new CommentWriteResult.Account(154L, "대댓글 회원", "http://../.jpg"))
    //         ),
    //         false
    //     );
    //     given(commentService.createComment(any(Account.class), any(CommentCreateParam.class)))
    //     .willReturn(createResult);
    //
    //     // when
    //     ResultActions resultActions = mockMvc.perform(post("/api/v1/comments")
    //         .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken())
    //         .contentType(MediaType.APPLICATION_JSON_VALUE)
    //         .content(objectMapper.writeValueAsString(createParam)));
    //
    //     // then
    //     resultActions
    //         .andDo(print())
    //         .andExpect(status().isCreated())
    //         .andDo(document("create-comment",
    //             getDocumentRequest(),
    //             getDocumentResponse(),
    //             requestHeaders(
    //                 headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
    //                 headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
    //             ),
    //             requestFields(
    //                 fieldWithPath("postId").description("실종 게시글 아이디"),
    //                 fieldWithPath("content").description("댓글 내용"),
    //                 fieldWithPath("parentCommentId").description("부모 댓글 아이디").optional()
    //             ),
    //             responseHeaders(
    //                 headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
    //             ),
    //             responseFields(
    //                 fieldWithPath("data").type(OBJECT).description("응답 데이터"),
    //                 fieldWithPath("data.id").type(NUMBER).description("댓글 아이디"),
    //                 fieldWithPath("data.content").type(STRING).description("댓글 내용"),
    //                 fieldWithPath("data.createdAt").type(STRING).description("댓글 작성날짜"),
    //                 fieldWithPath("data.deleted").type(BOOLEAN).description("삭제된 댓글 여부"),
    //                 fieldWithPath("data.account").type(OBJECT).description("댓글 작성자"),
    //                 fieldWithPath("data.account.id").type(NUMBER).description("작성자 아이디"),
    //                 fieldWithPath("data.account.nickname").type(STRING).description("작성자 닉네임"),
    //                 fieldWithPath("data.account.image").type(STRING).description("작성자 프로필 사진"),
    //                 fieldWithPath("data.childComments").type(ARRAY).description("댓글의 대댓글 목록"),
    //                 fieldWithPath("data.childComments[].id").type(NUMBER).description("대댓글 아이디"),
    //                 fieldWithPath("data.childComments[].content").type(STRING).description("대댓글 내용"),
    //                 fieldWithPath("data.childComments[].createdAt").type(STRING).description("대댓글 작성날짜"),
    //                 fieldWithPath("data.childComments[].account").type(OBJECT).description("대댓글 작성자"),
    //                 fieldWithPath("data.childComments[].account.id").type(NUMBER).description("작성자 아이디"),
    //                 fieldWithPath("data.childComments[].account.nickname").type(STRING)
    //                     .description("작성자 닉네임"),
    //                 fieldWithPath("data.childComments[].account.image").type(STRING)
    //                     .description("작성자 프로필 사진"),
    //                 fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
    //         );
    // }
    //
    // @Test
    // @WithAccount
    // @DisplayName("댓글 수정 테스트")
    // void updateCommentTest() throws Exception {
    //     // given
    //     CommentUpdateParam updateParam = CommentUpdateParam.builder()
    //         .content("updated content")
    //         .build();
    //     CommentWriteResult updateResult = new CommentWriteResult(
    //         123L,
    //         updateParam.getContent(),
    //         LocalDateTime.now(),
    //         new CommentWriteResult.Account(12L, "회원", "http://../.jpg"),
    //         List.of(new CommentWriteResult.ChildComment(
    //             13L,
    //             "자식 댓글",
    //             LocalDateTime.now(),
    //             new CommentWriteResult.Account(154L, "대댓글 회원", "http://../.jpg"))
    //         ),
    //         false
    //     );
    //     given(commentService.updateComment(anyLong(), any(CommentUpdateParam.class), any(Account.class)))
    //         .willReturn(updateResult);
    //
    //     // when
    //     ResultActions resultActions = mockMvc.perform(patch("/api/v1/comments/{commentId}", 1L)
    //         .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken())
    //         .contentType(MediaType.APPLICATION_JSON_VALUE)
    //         .content(objectMapper.writeValueAsString(updateParam)));
    //
    //     // then
    //     resultActions
    //         .andDo(print())
    //         .andExpect(status().isOk())
    //         .andDo(document("update-comment",
    //             getDocumentRequest(),
    //             getDocumentResponse(),
    //             pathParameters(
    //                 parameterWithName("commentId").description("댓글 아이디")
    //             ),
    //             requestHeaders(
    //                 headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
    //                 headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
    //             ),
    //             requestFields(
    //                 fieldWithPath("content").description("수정할 댓글 내용")
    //             ),
    //             responseHeaders(
    //                 headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
    //             ),
    //             responseFields(
    //                 fieldWithPath("data").type(OBJECT).description("응답 데이터"),
    //                 fieldWithPath("data.id").type(NUMBER).description("댓글 아이디"),
    //                 fieldWithPath("data.content").type(STRING).description("댓글 내용"),
    //                 fieldWithPath("data.createdAt").type(STRING).description("댓글 작성날짜"),
    //                 fieldWithPath("data.deleted").type(BOOLEAN).description("삭제된 댓글 여부"),
    //                 fieldWithPath("data.account").type(OBJECT).description("댓글 작성자"),
    //                 fieldWithPath("data.account.id").type(NUMBER).description("작성자 아이디"),
    //                 fieldWithPath("data.account.nickname").type(STRING).description("작성자 닉네임"),
    //                 fieldWithPath("data.account.image").type(STRING).description("작성자 프로필 사진"),
    //                 fieldWithPath("data.childComments").type(ARRAY).description("댓글의 대댓글 목록"),
    //                 fieldWithPath("data.childComments[].id").type(NUMBER).description("대댓글 아이디"),
    //                 fieldWithPath("data.childComments[].content").type(STRING).description("대댓글 내용"),
    //                 fieldWithPath("data.childComments[].createdAt").type(STRING).description("대댓글 작성날짜"),
    //                 fieldWithPath("data.childComments[].account").type(OBJECT).description("대댓글 작성자"),
    //                 fieldWithPath("data.childComments[].account.id").type(NUMBER).description("작성자 아이디"),
    //                 fieldWithPath("data.childComments[].account.nickname").type(STRING)
    //                     .description("작성자 닉네임"),
    //                 fieldWithPath("data.childComments[].account.image").type(STRING)
    //                     .description("작성자 프로필 사진"),
    //                 fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
    //         );
    // }
    //
    // @Test
    // @WithAccount
    // @DisplayName("댓글 삭제 테스트")
    // void deleteCommentTest() throws Exception {
    //     // given
    //     // when
    //     ResultActions resultActions = mockMvc.perform(delete("/api/v1/comments/{commentId}", 1L)
    //         .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken()));
    //
    //     // then
    //     resultActions
    //         .andDo(print())
    //         .andExpect(status().isNoContent())
    //         .andDo(document("delete-comment",
    //             getDocumentRequest(),
    //             getDocumentResponse(),
    //             pathParameters(
    //                 parameterWithName("commentId").description("댓글 아이디")
    //             ),
    //             requestHeaders(
    //                 headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
    //             ))
    //         );
    // }

}
