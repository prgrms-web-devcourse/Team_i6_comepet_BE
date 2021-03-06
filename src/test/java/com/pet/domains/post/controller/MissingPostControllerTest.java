package com.pet.domains.post.controller;

import static com.pet.domains.docs.utils.ApiDocumentUtils.getDocumentRequest;
import static com.pet.domains.docs.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.pet.domains.account.WithAccount;
import com.pet.domains.account.domain.Account;
import com.pet.domains.comment.dto.response.CommentPageResults;
import com.pet.domains.comment.dto.response.CommentPageResults.Comment;
import com.pet.domains.comment.dto.response.CommentPageResults.Comment.ChildComment;
import com.pet.domains.docs.BaseDocumentationTest;
import com.pet.domains.image.domain.Image;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import com.pet.domains.post.dto.request.MissingPostUpdateParam;
import com.pet.domains.post.dto.request.MissingPostUpdateParam.Tag;
import com.pet.domains.post.dto.response.MissingPostReadResult;
import com.pet.domains.post.dto.response.MissingPostReadResults;
import com.pet.domains.post.dto.response.MissingPostReadResults.MissingPost;
import com.pet.domains.post.dto.serach.PostSearchParam;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("??????/?????? ????????? ???????????? ?????????")
class MissingPostControllerTest extends BaseDocumentationTest {

    @DisplayName("??????/?????? ????????? ?????? ?????????")
    @WithAccount
    @Test
    void createMissingPostTest() throws Exception {
        //given
        MissingPostCreateParam param = MissingPostCreateParam.of(
            "DETECTION", LocalDate.now(), 1L, 1L, "???????????? ??? ?????? ??????",
            "01012343323", 1L, "??????", 10L, "MALE", "410123456789112",
            "??????????????? ?????????????????????.", List.of(
                MissingPostCreateParam.Tag.of("?????????")
            )
        );

        //when
        MockMultipartFile firstMultipartFile =
            new MockMultipartFile("images", "", "multipart/form-data", "abcd.jpg".getBytes());
        MockMultipartFile secondMultipartFile =
            new MockMultipartFile("images", "", "multipart/form-data", "abcd2.jpg".getBytes());
        MockMultipartFile paramFile =
            new MockMultipartFile("param", "", "application/json", objectMapper.writeValueAsString(param).getBytes(
                StandardCharsets.UTF_8));

        given(imageService.createImage(firstMultipartFile)).willReturn(mock(Image.class));

        ResultActions resultActions = mockMvc.perform(multipart("/api/v1/missing-posts")
            .file(firstMultipartFile)
            .file(secondMultipartFile)
            .file(paramFile)
            .contentType(MediaType.MULTIPART_MIXED)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken())
            .characterEncoding("UTF-8")
        );

        //then
        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("create-missing-post",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestParts(
                    partWithName("images").description("????????? ?????????"),
                    partWithName("param").description("????????? ?????? ?????? ?????????")
                ),
                requestPartFields(
                    "param",
                    fieldWithPath("status").type(STRING).description("<<status,????????? ??????>>"),
                    fieldWithPath("date").type(STRING).description("?????? ??????"),
                    fieldWithPath("cityId").type(NUMBER).description("?????? id"),
                    fieldWithPath("townId").type(NUMBER).description("????????? id"),
                    fieldWithPath("detailAddress").type(STRING).description("?????? ??? ?????? ??????").optional(),
                    fieldWithPath("telNumber").type(STRING).description("?????????"),
                    fieldWithPath("animalId").type(NUMBER).description("?????? id"),
                    fieldWithPath("animalKindName").type(STRING).description("?????? ??????"),
                    fieldWithPath("age").type(NUMBER).description("??????"),
                    fieldWithPath("sex").type(STRING).description("<<sexType,?????? ??????>>"),
                    fieldWithPath("chipNumber").type(STRING).description("?????????").optional(),
                    fieldWithPath("content").type(STRING).description("????????? ??????"),
                    fieldWithPath("tags").type(ARRAY).description("???????????? ???????????????").optional(),
                    fieldWithPath("tags[0].name").type(STRING).description("???????????? ??????").optional()
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????").optional(),
                    fieldWithPath("data.id").type(NUMBER).description("????????? id"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")
                )
            ));
    }

    @Test
    @WithAccount
    @DisplayName("??????/?????? ????????? ????????? ?????? ?????????")
    void getMissingPostsTest() throws Exception {
        //given
        MissingPostReadResults missingPostReadResults = MissingPostReadResults.of(List.of(
            MissingPost.of(
                1L, "???????????????", "?????????", "????????????", Status.DETECTION, LocalDateTime.now(),
                SexType.FEMALE, true, 2,
                "https://post-phinf.pstatic.net/MjAyMTA0MTJfNTAg/MDAxNjE4MjMwNjg1MTEw",
                List.of(
                    MissingPost.Tag.of(1L, "????????????"),
                    MissingPost.Tag.of(2L, "????????????"),
                    MissingPost.Tag.of(3L, "????????????")
                )
            )),
            10,
            true,
            5
        );
        given(missingPostService.getMissingPostsPageWithAccount(any(Account.class), any(PageRequest.class), any(
            PostSearchParam.class))).willReturn(
            missingPostReadResults);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/missing-posts")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken())
            .param("page", "1")
            .param("size", "10")
            .param("sort", "id,DESC"));

        // then
        resultActions
            .andExpect(status().isOk())
            .andDo(document("get-missing-posts",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token - optional").optional()
                ),
                requestParameters(
                    parameterWithName("page").description("????????? ??????"),
                    parameterWithName("size").description("????????? ??????"),
                    parameterWithName("sort").description("??????, ex) id,[desc]")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.missingPosts").type(ARRAY).description("??????/?????? ????????? ?????????"),
                    fieldWithPath("data.missingPosts[].id").type(NUMBER).description("????????? id"),
                    fieldWithPath("data.missingPosts[].city").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.missingPosts[].town").type(STRING).description("????????? ??????"),
                    fieldWithPath("data.missingPosts[].animalKindName").type(STRING).description("?????? ?????? ??????"),
                    fieldWithPath("data.missingPosts[].status").type(STRING).description("<<status,????????? ??????>>"),
                    fieldWithPath("data.missingPosts[].createdAt").type(STRING).description("????????? ????????????"),
                    fieldWithPath("data.missingPosts[].sex").type(STRING).description("<<sexType,?????? ??????>>"),
                    fieldWithPath("data.missingPosts[].thumbnail").type(STRING).description("????????? ?????????"),
                    fieldWithPath("data.missingPosts[].isBookmark").type(BOOLEAN).description("????????? ??????"),
                    fieldWithPath("data.missingPosts[].bookmarkCount").type(NUMBER).description("????????? ???"),
                    fieldWithPath("data.missingPosts[].tags").type(ARRAY).description("???????????? ??????"),
                    fieldWithPath("data.missingPosts[].tags[].id").type(NUMBER).description("???????????? id"),
                    fieldWithPath("data.missingPosts[].tags[].name").type(STRING).description("???????????? ??????"),
                    fieldWithPath("data.totalElements").type(NUMBER).description("?????? ????????? ???"),
                    fieldWithPath("data.last").type(BOOLEAN).description("????????? ????????? ??????"),
                    fieldWithPath("data.size").type(NUMBER).description("???????????? ?????? ???"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")))
            );
    }

    @Test
    @WithAccount
    @DisplayName("??????/?????? ????????? ?????? ?????? ?????????")
    void getMissingPostTest() throws Exception {
        //given
        MissingPostReadResult missingPostReadResult = MissingPostReadResult.of(1L,
            MissingPostReadResult.Account.of(1L, "??????",
                "https://img.insight.co.kr/static/2021/01/10/700/img_20210110130830_kue82l80.webp"
            ),
            Status.DETECTION, "2021-11-11", "?????????", "?????????", "???????????? ??? ?????? ??????",
            "01032430012", "???", "????????????", 10, SexType.MALE,
            "410123456789112",
            List.of(
                MissingPostReadResult.Image.of(1L, "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png"),
                MissingPostReadResult.Image.of(2L, "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png")
            ),
            List.of(
                MissingPostReadResult.Tag.of(1L, "????????????"),
                MissingPostReadResult.Tag.of(2L, "?????????")
            ),
            "??????????????? ????????? ?????????????????????. ???????????????", 3, 1, true, 1, LocalDateTime.now()
        );
        given(missingPostService.getMissingPostOneWithAccount(any(Account.class), anyLong(), anyBoolean())).willReturn(
            missingPostReadResult);

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/missing-posts/{postId}", 1L)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken()));

        // then
        resultActions
            .andExpect(status().isOk())
            .andDo(document("get-missing-post",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                    parameterWithName("postId").description("????????? id")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.id").type(NUMBER).description("????????? id"),
                    fieldWithPath("data.account").type(OBJECT).description("????????? ?????????"),
                    fieldWithPath("data.account.id").type(NUMBER).description("????????? ????????? id"),
                    fieldWithPath("data.account.nickname").type(STRING).description("????????? ????????? ?????????"),
                    fieldWithPath("data.account.image").type(STRING).description("????????? ????????? ????????? url"),
                    fieldWithPath("data.status").type(STRING).description("<<status,????????? ??????>>"),
                    fieldWithPath("data.date").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.city").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.town").type(STRING).description("????????? ??????"),
                    fieldWithPath("data.detailAddress").type(STRING).description("?????? ??? ?????? ??????"),
                    fieldWithPath("data.telNumber").type(STRING).description("?????????"),
                    fieldWithPath("data.animal").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.animalKindName").type(STRING).description("?????? ?????? ??????"),
                    fieldWithPath("data.age").type(NUMBER).description("?????? ??????"),
                    fieldWithPath("data.sex").type(STRING).description("<<sexType,?????? ??????>>"),
                    fieldWithPath("data.chipNumber").type(STRING).description("?????????"),
                    fieldWithPath("data.images").type(ARRAY).description("????????????"),
                    fieldWithPath("data.images[].id").type(NUMBER).description("????????? id"),
                    fieldWithPath("data.images[].name").type(STRING).description("????????? url"),
                    fieldWithPath("data.tags").type(ARRAY).description("???????????? ??????"),
                    fieldWithPath("data.tags[].id").type(NUMBER).description("???????????? id"),
                    fieldWithPath("data.tags[].name").type(STRING).description("???????????? ???"),
                    fieldWithPath("data.content").type(STRING).description("????????? ??????"),
                    fieldWithPath("data.viewCount").type(NUMBER).description("?????????"),
                    fieldWithPath("data.bookmarkCount").type(NUMBER).description("????????? ???"),
                    fieldWithPath("data.isBookmark").type(BOOLEAN).description("????????? ??????"),
                    fieldWithPath("data.commentCount").type(NUMBER).description("?????? ???"),
                    fieldWithPath("data.createdAt").type(STRING).description("????????? ????????????"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")))
            );
    }

    @Test
    @WithAccount
    @DisplayName("??????/?????? ????????? ?????? ?????????")
    void updateMissingPostTest() throws Exception {
        //given
        MissingPostUpdateParam param = MissingPostUpdateParam.of(
            Status.DETECTION, LocalDate.now(), 1L, 1L, "???????????? ??? ?????? ??????", "01034231111",
            1L, "??????", 10L, SexType.MALE, "410123456789112",
            List.of(
                Tag.of("?????????")
            ),
            "??????????????? ????????? ?????????????????????. ???????????????.",
            List.of(
                MissingPostUpdateParam.Image.of(1L, "abcddeee.jpg")
            )
        );
        MockMultipartFile multipartFile =
            new MockMultipartFile("images", "", "multipart/form-data", "abcd2.jpg".getBytes());
        MockMultipartFile paramFile =
            new MockMultipartFile("param", "", "application/json", objectMapper.writeValueAsString(param).getBytes(
                StandardCharsets.UTF_8));

        //when
        ResultActions resultActions = mockMvc.perform(multipart("/api/v1/missing-posts/{postId}", 1L)
            .file(multipartFile)
            .file(paramFile)
            .contentType(MediaType.MULTIPART_MIXED)
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken())
            .characterEncoding("UTF-8"));

        //then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("update-missing-post",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestParts(
                    partWithName("images").description("????????? ?????????"),
                    partWithName("param").description("????????? ?????? ?????? ?????????")
                ),
                requestPartFields(
                    "param",
                    fieldWithPath("status").type(STRING).description("<<status,????????? ??????>>"),
                    fieldWithPath("date").type(STRING).description("?????? ??????"),
                    fieldWithPath("cityId").type(NUMBER).description("?????? id"),
                    fieldWithPath("townId").type(NUMBER).description("????????? id"),
                    fieldWithPath("detailAddress").type(STRING).description("?????? ??? ?????? ??????").optional(),
                    fieldWithPath("telNumber").type(STRING).description("?????????"),
                    fieldWithPath("animalId").type(NUMBER).description("?????? id"),
                    fieldWithPath("animalKindName").type(STRING).description("?????? ??????"),
                    fieldWithPath("age").type(NUMBER).description("??????"),
                    fieldWithPath("sex").type(STRING).description("<<sexType,?????? ??????>>"),
                    fieldWithPath("chipNumber").type(STRING).description("?????????").optional(),
                    fieldWithPath("content").type(STRING).description("????????? ??????"),
                    fieldWithPath("tags").type(ARRAY).description("???????????? ???????????????").optional(),
                    fieldWithPath("tags[0].name").type(STRING).description("???????????? ??????").optional(),
                    fieldWithPath("images").type(ARRAY).description("???????????? ????????????").optional(),
                    fieldWithPath("images[0].id").type(NUMBER).description("????????? id").optional(),
                    fieldWithPath("images[0].name").type(STRING).description("????????? url").optional()
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.id").type(NUMBER).description("??????/?????? ????????? id"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")))
            );

    }

    @Test
    @WithAccount
    @DisplayName("??????/?????? ????????? ?????? ?????????")
    void deleteMissingPostTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/missing-posts/{postId}", 1L)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken()));

        // then
        resultActions
            .andExpect(status().isNoContent())
            .andDo(document("delete-missing-post",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                pathParameters(
                    parameterWithName("postId").description("??????/?????? ????????? id")
                ))
            );
    }

    @Test
    @WithAccount
    @DisplayName("??????/?????? ????????? ????????? ?????? ?????????")
    void createMissingPostBookmarkTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/missing-posts/{postId}/bookmark", 1L)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken()));

        // then
        resultActions
            .andExpect(status().isCreated())
            .andDo(document("create-missing-post-bookmark",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                pathParameters(
                    parameterWithName("postId").description("??????/?????? ????????? id")
                ))
            );
    }

    @Test
    @WithAccount
    @DisplayName("??????/?????? ????????? ????????? ?????? ?????????")
    void deleteMissingPostBookmarkTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/missing-posts/{postId}/bookmark", 1L)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken()));

        // then
        resultActions
            .andExpect(status().isNoContent())
            .andDo(document("delete-missing-post-bookmark",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                pathParameters(
                    parameterWithName("postId").description("??????/?????? ????????? id")
                ))
            );
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ????????? ?????? ?????????")
    void getMissingPostCommentsTest() throws Exception {
        // given
        CommentPageResults commentPageResults = new CommentPageResults(
            LongStream.rangeClosed(1, 2).mapToObj(idx -> new CommentPageResults.Comment(
                idx,
                "?????? ?????? #" + idx,
                LocalDateTime.now(),
                new Comment.Account(idx, "??????#" + idx, "http://../.jpg"),
                List.of(new ChildComment(
                    idx * 3,
                    "?????? ?????? #" + idx * 3,
                    LocalDateTime.now(),
                    new Comment.Account(idx * 3, "??????#" + idx * 3, "http://../.jpg"))
                ),
                false))
                .collect(Collectors.toList()),
            2,
            true,
            10
        );
        given(commentService.getMissingPostComments(anyLong(), any(PageRequest.class))).willReturn(commentPageResults);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/missing-posts/{postId}/comments", 1L)
            .accept(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-missing-post-comments",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                pathParameters(
                    parameterWithName("postId").description("?????? ????????? ?????????")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.comments").type(ARRAY).description("?????? ??????"),
                    fieldWithPath("data.comments[].id").type(NUMBER).description("?????? ?????????"),
                    fieldWithPath("data.comments[].content").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.comments[].createdAt").type(STRING).description("?????? ????????????"),
                    fieldWithPath("data.comments[].deleted").type(BOOLEAN).description("????????? ?????? ??????"),
                    fieldWithPath("data.comments[].account").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.comments[].account.id").type(NUMBER).description("????????? ?????????"),
                    fieldWithPath("data.comments[].account.nickname").type(STRING).description("????????? ?????????"),
                    fieldWithPath("data.comments[].account.image").type(STRING).description("????????? ????????? ??????"),
                    fieldWithPath("data.comments[].childComments").type(ARRAY).description("????????? ????????? ??????"),
                    fieldWithPath("data.comments[].childComments[].id").type(NUMBER).description("????????? ?????????"),
                    fieldWithPath("data.comments[].childComments[].content").type(STRING).description("????????? ??????"),
                    fieldWithPath("data.comments[].childComments[].createdAt").type(STRING).description("????????? ????????????"),
                    fieldWithPath("data.comments[].childComments[].account").type(OBJECT).description("????????? ?????????"),
                    fieldWithPath("data.comments[].childComments[].account.id").type(NUMBER).description("????????? ?????????"),
                    fieldWithPath("data.comments[].childComments[].account.nickname").type(STRING)
                        .description("????????? ?????????"),
                    fieldWithPath("data.comments[].childComments[].account.image").type(STRING)
                        .description("????????? ????????? ??????"),
                    fieldWithPath("data.totalElements").type(NUMBER).description("?????? ?????? ???"),
                    fieldWithPath("data.last").type(BOOLEAN).description("????????? ????????? ??????"),
                    fieldWithPath("data.size").type(NUMBER).description("???????????? ?????? ???"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")))
            );
    }

}
