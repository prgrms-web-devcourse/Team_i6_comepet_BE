package com.pet.domains.post.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.pet.common.jwt.JwtMockToken;
import com.pet.domains.docs.BaseDocumentationTest;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import com.pet.domains.post.dto.request.MissingPostUpdateParam;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("실종/보호 게시물 컨트롤러 테스트")
class MissingPostControllerTest extends BaseDocumentationTest {

    @DisplayName("실종/보호 게시물 등록 테스트")
    @Test
    void createMissingPostTest() throws Exception {
        //given
        MissingPostCreateParam param = MissingPostCreateParam.of(
            "DETECTION", LocalDate.now(), 1L, 1L, "주민센터 앞 골목 근처",
            "01012343323", 1L, 1L, 10, "MALE", "410123456789112",
            "찾아주시면 사례하겠습니다.", List.of(
                MissingPostCreateParam.PostTag.of("춘식이")
            )
        );

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/missing-posts")
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(param)));

        //then
        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("create-missing-post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestFields(
                    fieldWithPath("status").type(STRING).description("<<status,게시물 상태>>"),
                    fieldWithPath("date").type(STRING).description("발견 날짜"),
                    fieldWithPath("cityId").type(NUMBER).description("시도 id"),
                    fieldWithPath("townId").type(NUMBER).description("시군구 id"),
                    fieldWithPath("detailAddress").type(STRING).description("상세 및 추가 주소").optional(),
                    fieldWithPath("telNumber").type(STRING).description("연락처"),
                    fieldWithPath("animalId").type(NUMBER).description("동물 id").optional(),
                    fieldWithPath("animalKindId").type(NUMBER).description("품종 id").optional(),
                    fieldWithPath("age").type(NUMBER).description("나이").optional(),
                    fieldWithPath("sex").type(STRING).description("<<sexType,동물 성별>>"),
                    fieldWithPath("chipNumber").type(STRING).description("칩번호").optional(),
                    fieldWithPath("content").type(STRING).description("게시물 내용").optional(),
                    fieldWithPath("postTags").type(ARRAY).description("게시글의 해시태그들").optional(),
                    fieldWithPath("postTags[0].name").type(STRING).description("해시태그 내용").optional()
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터").optional(),
                    fieldWithPath("data.id").type(NUMBER).description("회원 id"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")
                )
            ));
    }

    @Test
    @DisplayName("실종/보호 게시물 리스트 조회 테스트")
    void getMissingPostsTest() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/missing-posts")
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN)
            .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
            .andExpect(status().isOk())
            .andDo(document("get-missing-posts",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.missingPosts").type(ARRAY).description("실종/보호 게시물 리스트"),
                    fieldWithPath("data.missingPosts[].id").type(NUMBER).description("게시글 id"),
                    fieldWithPath("data.missingPosts[].city").type(STRING).description("시도 이름"),
                    fieldWithPath("data.missingPosts[].town").type(STRING).description("시군구 이름"),
                    fieldWithPath("data.missingPosts[].animalKind").type(STRING).description("동물 품종"),
                    fieldWithPath("data.missingPosts[].status").type(STRING).description("<<status,게시물 상태>>"),
                    fieldWithPath("data.missingPosts[].createdAt").type(STRING).description("게시글 작성날짜"),
                    fieldWithPath("data.missingPosts[].sex").type(STRING).description("<<sexType,동물 성별>>"),
                    fieldWithPath("data.missingPosts[].thumbnail").type(STRING).description("게시글 썸네일"),
                    fieldWithPath("data.missingPosts[].isBookmark").type(BOOLEAN).description("북마크 여부"),
                    fieldWithPath("data.missingPosts[].bookmarkCount").type(NUMBER).description("북마크 수"),
                    fieldWithPath("data.missingPosts[].postTags").type(ARRAY).description("해시태그 배열"),
                    fieldWithPath("data.missingPosts[].postTags[].id").type(NUMBER).description("해시태그 id"),
                    fieldWithPath("data.missingPosts[].postTags[].name").type(STRING).description("해시태그 내용"),
                    fieldWithPath("data.totalElements").type(NUMBER).description("전체 게시물 수"),
                    fieldWithPath("data.last").type(BOOLEAN).description("마지막 페이지 여부"),
                    fieldWithPath("data.size").type(NUMBER).description("페이지당 요청 수"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );
    }

    @Test
    @DisplayName("실종/보호 게시물 단건 조회 테스트")
    void getMissingPostTest() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/missing-posts/{postId}", 1L)
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN)
            .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
            .andExpect(status().isOk())
            .andDo(document("get-missing-post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                pathParameters(
                    parameterWithName("postId").description("게시글 id")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.id").type(NUMBER).description("게시글 id"),
                    fieldWithPath("data.user").type(OBJECT).description("게시글 작성자"),
                    fieldWithPath("data.user.id").type(NUMBER).description("게시글 작성자 id"),
                    fieldWithPath("data.user.nickname").type(STRING).description("게시글 작성자 닉네임"),
                    fieldWithPath("data.user.image").type(STRING).description("게시글 작성자 프로필 url"),
                    fieldWithPath("data.status").type(STRING).description("<<status,게시물 상태>>"),
                    fieldWithPath("data.date").type(STRING).description("상태 날짜"),
                    fieldWithPath("data.city").type(STRING).description("시도 이름"),
                    fieldWithPath("data.town").type(STRING).description("시군구 이름"),
                    fieldWithPath("data.detailAddress").type(STRING).description("상세 및 추가 주소"),
                    fieldWithPath("data.telNumber").type(STRING).description("연락처"),
                    fieldWithPath("data.animal").type(STRING).description("동물 종류"),
                    fieldWithPath("data.animalKind").type(STRING).description("동물 품종"),
                    fieldWithPath("data.age").type(NUMBER).description("동물 나이"),
                    fieldWithPath("data.sex").type(STRING).description("<<sexType,동물 성별>>"),
                    fieldWithPath("data.chipNumber").type(STRING).description("칩번호"),
                    fieldWithPath("data.postImages").type(ARRAY).description("이미지들"),
                    fieldWithPath("data.postImages[].id").type(NUMBER).description("이미지 id"),
                    fieldWithPath("data.postImages[].name").type(STRING).description("이미지 url"),
                    fieldWithPath("data.postTags").type(ARRAY).description("해시태그 배열"),
                    fieldWithPath("data.postTags[].id").type(NUMBER).description("해시태그 id"),
                    fieldWithPath("data.postTags[].name").type(STRING).description("해시태그 값"),
                    fieldWithPath("data.content").type(STRING).description("게시글 내용"),
                    fieldWithPath("data.viewCount").type(NUMBER).description("조회수"),
                    fieldWithPath("data.bookmarkCount").type(NUMBER).description("북마크 수"),
                    fieldWithPath("data.isBookmark").type(BOOLEAN).description("북마크 여부"),
                    fieldWithPath("data.commentCount").type(NUMBER).description("댓글 수"),
                    fieldWithPath("data.comments").type(ARRAY).description("댓글들"),
                    fieldWithPath("data.comments[].id").type(NUMBER).description("댓글 id"),
                    fieldWithPath("data.comments[].user").type(OBJECT).description("댓글 작성자"),
                    fieldWithPath("data.comments[].user.id").type(NUMBER).description("댓글 작성자 id"),
                    fieldWithPath("data.comments[].user.nickname").type(STRING).description("댓글 작성자 닉네임"),
                    fieldWithPath("data.comments[].user.image").type(STRING).description("댓글 작성자 프로필 url"),
                    fieldWithPath("data.comments[].content").type(STRING).description("댓글 내용"),
                    fieldWithPath("data.comments[].createdAt").type(STRING).description("댓글 작성 날짜"),
                    fieldWithPath("data.createdAt").type(STRING).description("게시글 작성날짜"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );
    }

    @Test
    @DisplayName("실종/보호 게시물 수정 테스트")
    void updateMissingPostTest() throws Exception {
        //given
        MissingPostUpdateParam param = MissingPostUpdateParam.of(
            Status.DETECTION, LocalDate.now(), 1L, 1L, "주민센터 앞 골목 근처", "01034231111",
            1L, 1L, 10, SexType.MALE, "410123456789112",
            List.of(
                MissingPostUpdateParam.PostTag.of("춘식이")
            ), "찾아주시면 반드시 사례하겠습니다. 연락주세요."
        );

        //when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/missing-posts/{postId}", 1L)
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(param)));

        //then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("update-missing-post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                pathParameters(
                    parameterWithName("postId").description("실종/보호 게시물 id")
                ),
                requestFields(
                    fieldWithPath("status").type(STRING).description("<<status,게시물 상태>>"),
                    fieldWithPath("date").type(STRING).description("날짜"),
                    fieldWithPath("city").type(NUMBER).description("시도 id"),
                    fieldWithPath("town").type(NUMBER).description("시군구 id"),
                    fieldWithPath("detailAddress").type(STRING).description("상세 및 추가 주소"),
                    fieldWithPath("telNumber").type(STRING).description("연락처"),
                    fieldWithPath("animal").type(NUMBER).description("동물 종류 id"),
                    fieldWithPath("animalKind").type(NUMBER).description("품종 종류 id"),
                    fieldWithPath("age").type(NUMBER).description("나이"),
                    fieldWithPath("sex").type(STRING).description("<<sexType,동물 성별>>"),
                    fieldWithPath("chipNumber").type(STRING).description("칩번호"),
                    fieldWithPath("postTags").type(ARRAY).description("해시태그 배열"),
                    fieldWithPath("postTags[0].name").type(STRING).description("해시태그 내용"),
                    fieldWithPath("content").type(STRING).description("실종/보호 내용")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.id").type(NUMBER).description("실종/보호 게시물 id"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );

    }

    @Test
    @DisplayName("실종/보호 게시물 삭제 테스트")
    void deleteMissingPostTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/missing-posts/{postId}", 1L)
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN));

        // then
        resultActions
            .andExpect(status().isNoContent())
            .andDo(document("delete-missing-post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                pathParameters(
                    parameterWithName("postId").description("실종/보호 게시물 id")
                ))
            );
    }

    @Test
    @DisplayName("실종/보호 게시글 북마크 생성 테스트")
    void createMissingPostBookmarkTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/missing-posts/{postId}/bookmark", 1L)
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN));

        // then
        resultActions
            .andExpect(status().isCreated())
            .andDo(document("create-missing-post-bookmark",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                pathParameters(
                    parameterWithName("postId").description("실종/보호 게시글 id")
                ))
            );
    }

    @Test
    @DisplayName("실종/보호 게시글 북마크 삭제 테스트")
    void deleteMissingPostBookmarkTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/missing-posts/{postId}/bookmark", 1L)
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN));

        // then
        resultActions
            .andExpect(status().isNoContent())
            .andDo(document("delete-missing-post-bookmark",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                pathParameters(
                    parameterWithName("postId").description("실종/보호 게시글 id")
                ))
            );
    }

    @Test
    @DisplayName("특정 실종게시글의 댓글 리스트 조회 테스트")
    void getShelterPostsTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/missing-posts/{postId}/comments", 1L)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-missing-posts-comments",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("postId").description("실종 게시글 아이디")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
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
