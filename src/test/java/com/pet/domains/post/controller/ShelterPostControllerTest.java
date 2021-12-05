package com.pet.domains.post.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

@WebMvcTest(ShelterPostController.class)
@AutoConfigureRestDocs
@DisplayName("보호소 동물 게시글 컨트롤러 docs 테스트")
class ShelterPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("보호소 게시글 리스트 조회 테스트")
    void getShelterPostsTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/shelter-posts")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-shelter-posts",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.shelters").type(ARRAY).description("보호소 게시물 리스트"),
                    fieldWithPath("data.shelters[].id").type(NUMBER).description("게시글 id"),
                    fieldWithPath("data.shelters[].city").type(STRING).description("시도 이름"),
                    fieldWithPath("data.shelters[].town").type(STRING).description("시군구 이름"),
                    fieldWithPath("data.shelters[].age").type(NUMBER).description("동물 나이"),
                    fieldWithPath("data.shelters[].image").type(STRING).description("동물 사진"),
                    fieldWithPath("data.shelters[].animal").type(STRING).description("동물 종류"),
                    fieldWithPath("data.shelters[].animalKind").type(STRING).description("동물 품종"),
                    fieldWithPath("data.shelters[].foundDate").type(STRING).description("접수일"),
                    fieldWithPath("data.shelters[].shelterPlace").type(STRING).description("보호 장소"),
                    fieldWithPath("data.shelters[].isBookmark").type(BOOLEAN).description("북마크 여부"),
                    fieldWithPath("data.shelters[].bookmarkCount").type(NUMBER).description("북마크 수"),
                    fieldWithPath("data.totalElements").type(NUMBER).description("전체 결과 수"),
                    fieldWithPath("data.last").type(BOOLEAN).description("마지막 페이지 여부"),
                    fieldWithPath("data.size").type(NUMBER).description("페이지당 요청 수"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );
    }

    @Test
    @DisplayName("보호소 게시글 단건 조회 테스트")
    void getShelterPostTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/shelter-posts/{postId}", 1L)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-shelter-post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("postId").description("게시글 아이디")
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
                    fieldWithPath("data.id").type(NUMBER).description("게시글 id"),
                    fieldWithPath("data.age").type(NUMBER).description("동물 나이"),
                    fieldWithPath("data.shelterPlace").type(STRING).description("보호 장소"),
                    fieldWithPath("data.shelterName").type(STRING).description("보호소 이름"),
                    fieldWithPath("data.shelterTelNumber").type(STRING).description("보호소 전화번호"),
                    fieldWithPath("data.color").type(STRING).description("동물 색상"),
                    fieldWithPath("data.image").type(STRING).description("동물 사진"),
                    fieldWithPath("data.foundDate").type(STRING).description("접수일"),
                    fieldWithPath("data.foundPlace").type(STRING).description("발견 장소"),
                    fieldWithPath("data.animal").type(STRING).description("동물 종류"),
                    fieldWithPath("data.animalKind").type(STRING).description("동물 품종"),
                    fieldWithPath("data.neutered").type(STRING).description("중성화 여부"),
                    fieldWithPath("data.startDate").type(STRING).description("공고 시작일"),
                    fieldWithPath("data.endDate").type(STRING).description("공고 마감일"),
                    fieldWithPath("data.noticeNumber").type(STRING).description("공고 번호"),
                    fieldWithPath("data.managerTelNumber").type(STRING).description("담당자 연락처"),
                    fieldWithPath("data.status").type(STRING).description("공고 상태"),
                    fieldWithPath("data.sex").type(STRING).description("동물 성별"),
                    fieldWithPath("data.feature").type(STRING).description("동물 특징"),
                    fieldWithPath("data.weight").type(NUMBER).description("동물 체중"),
                    fieldWithPath("data.isBookmark").type(BOOLEAN).description("북마크 여부"),
                    fieldWithPath("data.bookmarkCount").type(NUMBER).description("북마크 수"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );
    }

    @Test
    @DisplayName("보호소 게시글 북마크 생성 테스트")
    void createShelterPostBookmarkTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/shelter-posts/{postId}/bookmark", 1L)
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("create-shelter-post-bookmark",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("postId").description("게시글 아이디")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ))
            );
    }

    @Test
    @DisplayName("보호소 게시글 북마크 삭제 테스트")
    void deleteShelterPostBookmarkTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/shelter-posts/{postId}/bookmark", 1L)
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(document("delete-shelter-post-bookmark",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("postId").description("게시글 아이디")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ))
            );
    }
}
