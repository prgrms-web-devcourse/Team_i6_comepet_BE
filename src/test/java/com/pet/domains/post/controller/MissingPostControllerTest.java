package com.pet.domains.post.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MissingPostController.class)
@AutoConfigureRestDocs
@DisplayName("실종/보호 게시물 컨트롤러 테스트")
class MissingPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("실종/보호 게시물 등록 테스트")
    @Test
    void createMissingPostTest() throws Exception {
        //given
        MissingPostCreateParam param = MissingPostCreateParam.of(
            "DETECTION", LocalDate.now(), 1L, 1L, "주민센터 앞 골목 근처",
            "01012343323", 1L, 1L, 10, "MALE", "410123456789112",
            "찾아주시면 사례하겠습니다.", null
        );

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/missing-posts")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(param)));

        //then
        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("create-missingPost",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestFields(
                    fieldWithPath("status").type(STRING).description("게시글 상태"),
                    fieldWithPath("date").type(STRING).description("발견 날짜"),
                    fieldWithPath("cityId").type(NUMBER).description("시도 Id"),
                    fieldWithPath("townId").type(NUMBER).description("시군구 Id"),
                    fieldWithPath("detailAddress").type(STRING).description("상세 및 추가 주소").optional(),
                    fieldWithPath("telNumber").type(STRING).description("연락처"),
                    fieldWithPath("animalId").type(NUMBER).description("동물 Id").optional(),
                    fieldWithPath("animalKindId").type(NUMBER).description("품종 Id").optional(),
                    fieldWithPath("age").type(NUMBER).description("나이").optional(),
                    fieldWithPath("sex").type(STRING).description("성별"),
                    fieldWithPath("chipNumber").type(STRING).description("칩번호").optional(),
                    fieldWithPath("content").type(STRING).description("게시물 내용").optional(),
                    fieldWithPath("postTags").type(NULL).description("게시글의 해시태그들").optional()
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
            .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
            .andExpect(status().isOk())
            .andDo(document("get-missingPosts",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.missingPosts").type(ARRAY).description("실종/보호 게시물 리스트"),
                    fieldWithPath("data.missingPosts[].id").type(NUMBER).description("게시글 Id"),
                    fieldWithPath("data.missingPosts[].city").type(STRING).description("시도 이름"),
                    fieldWithPath("data.missingPosts[].town").type(STRING).description("시군구 이름"),
                    fieldWithPath("data.missingPosts[].animalKind").type(STRING).description("동물 품종"),
                    fieldWithPath("data.missingPosts[].status").type(STRING).description("게시글 상태"),
                    fieldWithPath("data.missingPosts[].createdAt").type(STRING).description("게시글 작성날짜"),
                    fieldWithPath("data.missingPosts[].sex").type(STRING).description("동물 성별"),
                    fieldWithPath("data.missingPosts[].thumbnail").type(STRING).description("게시글 썸네일"),
                    fieldWithPath("data.missingPosts[].isBookmark").type(BOOLEAN).description("북마크 여부"),
                    fieldWithPath("data.missingPosts[].bookmarkCount").type(NUMBER).description("북마크 수"),
                    fieldWithPath("data.missingPosts[].postTags").type(ARRAY).description("해시태그 배열"),
                    fieldWithPath("data.missingPosts[].postTags[].id").type(NUMBER).description("해시태그 Id"),
                    fieldWithPath("data.missingPosts[].postTags[].name").type(STRING).description("해시태그 내용"),
                    fieldWithPath("data.totalElements").type(NUMBER).description("전체 게시물 수"),
                    fieldWithPath("data.last").type(BOOLEAN).description("마지막 페이지 여부"),
                    fieldWithPath("data.size").type(NUMBER).description("페이지당 요청 수"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );
    }

}
