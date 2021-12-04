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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
    @DisplayName("동물/품종 조회 성공 테스트")
    void getAnimalsTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/shelter-posts")
            .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(document("get-shelter-posts",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
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
}