package com.pet.domains.post.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MissingPostController.class)
@AutoConfigureRestDocs
@DisplayName("실종/보호 게시물 테스트")
class MissingPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("게시물 등록 테스트")
    @Test
    void createMissingPostTest() throws Exception {
        //given
        MissingPostCreateParam param = new MissingPostCreateParam("DETECTION", LocalDate.now(), 1L,
            1L, "주민센터 앞 골목 근처", "01012343323", 1L, 1L, 10,
            "MALE", "410123456789112", "찾아주시면 사례하겠습니다.", null);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/missing-posts")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(param)));

        //then
        resultActions
            .andDo(print())
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
                    fieldWithPath("status").type(JsonFieldType.STRING).description("게시글 상태"),
                    fieldWithPath("date").type(JsonFieldType.STRING).description("발견 날짜"),
                    fieldWithPath("cityId").type(JsonFieldType.NUMBER).description("시도 Id"),
                    fieldWithPath("townId").type(JsonFieldType.NUMBER).description("시군구 Id"),
                    fieldWithPath("detailAddress").type(JsonFieldType.STRING).description("상세 및 추가 주소"),
                    fieldWithPath("telNumber").type(JsonFieldType.STRING).description("연락처"),
                    fieldWithPath("animalId").type(JsonFieldType.NUMBER).description("동물 Id"),
                    fieldWithPath("animalKindId").type(JsonFieldType.NUMBER).description("품종 Id"),
                    fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이"),
                    fieldWithPath("sex").type(JsonFieldType.STRING).description("성별"),
                    fieldWithPath("chipNumber").type(JsonFieldType.STRING).description("칩번호"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("게시물 내용"),
                    fieldWithPath("postTags").type(JsonFieldType.NULL).description("게시글의 해시태그들")
                ),
                responseFields(
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터").optional(),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 id"),
                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("서버 응답 시간")
                )
            ));
    }

}
