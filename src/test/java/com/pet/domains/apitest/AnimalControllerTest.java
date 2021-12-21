package com.pet.domains.apitest;

import static com.pet.domains.docs.utils.ApiDocumentUtils.getDocumentRequest;
import static com.pet.domains.docs.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.common.property.JwtProperty;
import com.pet.domains.account.controller.AccountController;
import com.pet.domains.account.service.AccountService;
import com.pet.domains.animal.controller.AnimalController;
import com.pet.domains.animal.dto.response.AnimalReadResults;
import com.pet.domains.animal.dto.response.AnimalReadResults.Animal;
import com.pet.domains.animal.dto.response.AnimalReadResults.Animal.AnimalKind;
import com.pet.domains.animal.service.AnimalService;
import com.pet.domains.docs.BaseDocumentationTest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AnimalController.class)
@AutoConfigureRestDocs
@EnableConfigurationProperties(value = JwtProperty.class)
@DisplayName("동물 컨트롤러 테스트")
class AnimalControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected AccountService accountService;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AnimalService animalService;

    @Test
    @DisplayName("동물/품종 조회 성공 테스트")
    void getAnimalsTest() throws Exception {
        // given
        List<AnimalReadResults.Animal.AnimalKind> animalKinds = LongStream.rangeClosed(1, 5)
            .mapToObj(id -> AnimalKind.builder()
                .id(id)
                .name("kindName#" + id)
                .build())
            .collect(Collectors.toList());
        AnimalReadResults.Animal animal = Animal.builder()
            .id(1L)
            .name("name")
            .kinds(animalKinds)
            .build();
        given(animalService.getAnimals()).willReturn(AnimalReadResults.of(List.of(animal)));

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/animals")
            .accept(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-animals",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.animals").type(ARRAY).description("동물"),
                    fieldWithPath("data.animals[0].id").type(NUMBER).description("동물 id"),
                    fieldWithPath("data.animals[0].name").type(STRING).description("동물 이름"),
                    fieldWithPath("data.animals[0].kinds").type(ARRAY).description("품종 id"),
                    fieldWithPath("data.animals[0].kinds[0].id").type(NUMBER).description("품종 id"),
                    fieldWithPath("data.animals[0].kinds[0].name").type(STRING).description("품종 id"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );
    }
}
