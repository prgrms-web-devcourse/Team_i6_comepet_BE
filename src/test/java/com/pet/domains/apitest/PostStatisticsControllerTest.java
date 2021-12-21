package com.pet.domains.apitest;

import static com.pet.domains.docs.utils.ApiDocumentUtils.getDocumentRequest;
import static com.pet.domains.docs.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.common.config.SecurityConfig;
import com.pet.common.jwt.JwtAuthentication;
import com.pet.common.property.JwtProperty;
import com.pet.domains.account.service.AccountService;
import com.pet.domains.account.service.NotificationService;
import com.pet.domains.area.service.CityService;
import com.pet.domains.docs.BaseDocumentationTest;
import com.pet.domains.post.controller.MissingPostController;
import com.pet.domains.statistics.controller.PostStatisticsController;
import com.pet.domains.statistics.dto.response.PostStatisticsReadResult;
import com.pet.domains.statistics.service.PostStatisticsService;
import java.time.LocalDateTime;
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

@WebMvcTest(value = PostStatisticsController.class,
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
})
@AutoConfigureRestDocs
@EnableConfigurationProperties(value = JwtProperty.class)
@DisplayName("통계 컨트롤러 테스트")
class PostStatisticsControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AccountService accountService;

    @MockBean
    protected PostStatisticsService postStatisticsService;

    @Test
    @DisplayName("게시글 통계 데이터 조회 테스트")
    void getPostStatisticsTest() throws Exception {
        // given
        PostStatisticsReadResult result = PostStatisticsReadResult.builder()
            .missing(5312L)
            .detection(312L)
            .protection(142L)
            .completion(4213L)
            .date(LocalDateTime.now())
            .build();
        given(postStatisticsService.getPostStatistics()).willReturn(result);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/statistics")
            .accept(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-post-statistics",
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
                    fieldWithPath("data.missing").type(NUMBER).description("실종 게시글 수"),
                    fieldWithPath("data.detection").type(NUMBER).description("목격 게시글 수"),
                    fieldWithPath("data.protection").type(NUMBER).description("보호 게시글 수"),
                    fieldWithPath("data.completion").type(NUMBER).description("완료 게시글 수"),
                    fieldWithPath("data.date").type(STRING).description("통계 시간"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );
    }
}
