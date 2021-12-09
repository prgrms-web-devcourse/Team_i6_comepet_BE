package com.pet.domains.docs.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pet.common.response.ApiResponse;
import com.pet.domains.docs.BaseDocumentationTest;
import com.pet.domains.docs.CustomResponseFieldsSnippet;
import com.pet.domains.docs.dto.CommonDocumentationResults;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

class CommonDocumentationControllerTest extends BaseDocumentationTest {

    @Test
    void commonDocumentationTest() throws Exception {

        // given
        // when
        ResultActions resultActions = this.mockMvc.perform(
            get("/common")
                .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        MvcResult mvcResult = resultActions.andReturn();
        CommonDocumentationResults results = getData(mvcResult);

        // then
        resultActions.andExpect(status().isOk())
            .andDo(document("common",
                customResponseFields("custom-response", null,
                    attributes(key("title").value("공통응답")),
                    subsectionWithPath("data").type(OBJECT).description("데이터"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 시간")
                ),
                customResponseFields("custom-response", beneathPath("data.sexTypes").withSubsectionId("sexTypes"),
                    attributes(key("title").value("성별")),
                    enumConvertFieldDescriptor(results.getSexTypes())
                ),
                customResponseFields("custom-response",
                    beneathPath("data.neuteredTypes").withSubsectionId("neuteredTypes"),
                    attributes(key("title").value("중성화 여부")),
                    enumConvertFieldDescriptor(results.getNeuteredTypes())
                ),
                customResponseFields("custom-response", beneathPath("data.status").withSubsectionId("status"),
                    attributes(key("title").value("게시글 상태")),
                    enumConvertFieldDescriptor(results.getStatus())
                )
            ));
    }

    private static FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {

        return enumValues.entrySet().stream()
            .map(value -> fieldWithPath(value.getKey()).type(STRING).description(value.getValue()))
            .toArray(FieldDescriptor[]::new);
    }

    CommonDocumentationResults getData(MvcResult result) throws IOException {
        ApiResponse<CommonDocumentationResults> apiResponse = objectMapper.readValue(
            result.getResponse().getContentAsByteArray(),
            new TypeReference<>() {
            });

        return apiResponse.getData();
    }

    public static CustomResponseFieldsSnippet customResponseFields(
        String type,
        PayloadSubsectionExtractor<?> subsectionExtractor,
        Map<String, Object> attributes, FieldDescriptor... descriptors
    ) {
        return new CustomResponseFieldsSnippet(
            type,
            subsectionExtractor,
            Arrays.asList(descriptors),
            attributes,
            true
        );
    }

}
