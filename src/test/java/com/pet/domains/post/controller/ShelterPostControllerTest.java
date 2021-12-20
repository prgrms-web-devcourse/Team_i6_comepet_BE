package com.pet.domains.post.controller;

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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.pet.domains.account.WithAccount;
import com.pet.domains.account.domain.Account;
import com.pet.domains.docs.BaseDocumentationTest;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.dto.response.ShelterPostPageResults;
import com.pet.domains.post.dto.response.ShelterPostReadResult;
import com.pet.domains.post.dto.serach.PostSearchParam;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("보호소 동물 게시글 컨트롤러 테스트")
class ShelterPostControllerTest extends BaseDocumentationTest {

    @Test
    @WithAccount
    @DisplayName("보호소 게시글 리스트 조회 테스트")
    void getShelterPostsTest() throws Exception {
        // given
        var results = ShelterPostPageResults.of(List.of(
            ShelterPostPageResults.ShelterPost.builder()
                .id(1L)
                .city("서울특별시")
                .town("광진구")
                .age(2018L)
                .sex(SexType.UNKNOWN)
                .thumbnail("http://www.animal.go.kr/files/shelter/2021/11/202112140012452_s.jpg")
                .animal("개")
                .animalKindName("보더콜리")
                .foundDate(LocalDate.of(2021, 12, 11))
                .isBookmark(true)
                .bookmarkCount(13)
                .build()),
            1,
            true,
            10
        );
        given(shelterPostService.getShelterPostsPageWithAccount(any(Account.class), any(PageRequest.class), any(
            PostSearchParam.class)))
            .willReturn(results);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/shelter-posts")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken())
            .param("page", "1")
            .param("size", "10")
            .param("sort", "id,DESC"));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-shelter-posts",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token - optional").optional()
                ),
                requestParameters(
                    parameterWithName("page").description("페이지 번호"),
                    parameterWithName("size").description("페이지 크기"),
                    parameterWithName("sort").description("정렬, ex) id,[desc]")
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
                    fieldWithPath("data.shelters[].sex").type(STRING).description("<<sexType,동물 성별>>>>"),
                    fieldWithPath("data.shelters[].thumbnail").type(STRING).description("동물 사진"),
                    fieldWithPath("data.shelters[].animal").type(STRING).description("동물 종류"),
                    fieldWithPath("data.shelters[].animalKindName").type(STRING).description("동물 품종"),
                    fieldWithPath("data.shelters[].foundDate").type(STRING).description("접수일"),
                    fieldWithPath("data.shelters[].isBookmark").type(BOOLEAN).description("북마크 여부"),
                    fieldWithPath("data.shelters[].bookmarkCount").type(NUMBER).description("북마크 수"),
                    fieldWithPath("data.totalElements").type(NUMBER).description("전체 데이터수"),
                    fieldWithPath("data.last").type(BOOLEAN).description("마지막 페이지 여부"),
                    fieldWithPath("data.size").type(NUMBER).description("페이지 크기"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );
    }

    @Test
    @WithAccount
    @DisplayName("보호소 게시글 단건 조회 테스트")
    void getShelterPostTest() throws Exception {
        // given
        var results = ShelterPostReadResult.builder()
            .id(1L)
            .city("서울특별시")
            .town("광진구")
            .age(2018L)
            .image("http://www.animal.go.kr/files/shelter/2021/11/202112140012452_s.jpg")
            .animal("개")
            .animalKindName("보더콜리")
            .foundDate(LocalDate.of(2021, 12, 11))
            .isBookmark(true)
            .bookmarkCount(13L)
            .color("흰색")
            .startDate(LocalDate.of(2021, 10, 1))
            .endDate(LocalDate.of(2021, 12, 1))
            .feature("상태양호")
            .foundPlace("전라남도 화순군 동면 동림길 3-15")
            .managerTelNumber("055-749-6134")
            .neutered("N")
            .shelterName("화순군유기동물보호소")
            .noticeNumber("전남-화순-2021-00262")
            .sex("MALE")
            .status("보호중")
            .weight(15.0)
            .shelterTelNumber("055-749-6134")
            .isBookmark(true)
            .build();
        given(shelterPostService.getShelterPostReadResultWithAccount(any(Account.class), anyLong()))
            .willReturn(results);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/shelter-posts/{postId}", 1L)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken()));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-shelter-post",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                    parameterWithName("postId").description("게시글 아이디")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token - optional").optional()
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.id").type(NUMBER).description("게시글 id"),
                    fieldWithPath("data.age").type(NUMBER).description("동물 나이"),
                    fieldWithPath("data.city").type(STRING).description("시도 이름"),
                    fieldWithPath("data.town").type(STRING).description("시군구 이름"),
                    fieldWithPath("data.shelterName").type(STRING).description("보호소 이름"),
                    fieldWithPath("data.shelterTelNumber").type(STRING).description("보호소 전화번호"),
                    fieldWithPath("data.shelterPlace").type(STRING).description("보호 장소"),
                    fieldWithPath("data.color").type(STRING).description("동물 색상"),
                    fieldWithPath("data.image").type(STRING).description("동물 사진"),
                    fieldWithPath("data.foundDate").type(STRING).description("접수일"),
                    fieldWithPath("data.foundPlace").type(STRING).description("발견 장소"),
                    fieldWithPath("data.animal").type(STRING).description("동물 종류"),
                    fieldWithPath("data.animalKindName").type(STRING).description("동물 품종"),
                    fieldWithPath("data.neutered").type(STRING).description("<<neuteredType,중성화 여부>>"),
                    fieldWithPath("data.startDate").type(STRING).description("공고 시작일"),
                    fieldWithPath("data.endDate").type(STRING).description("공고 마감일"),
                    fieldWithPath("data.noticeNumber").type(STRING).description("공고 번호"),
                    fieldWithPath("data.managerTelNumber").type(STRING).description("담당자 연락처"),
                    fieldWithPath("data.status").type(STRING).description("공고 상태"),
                    fieldWithPath("data.sex").type(STRING).description("<<sexType,동물 성별>>"),
                    fieldWithPath("data.feature").type(STRING).description("동물 특징"),
                    fieldWithPath("data.weight").type(NUMBER).description("동물 체중"),
                    fieldWithPath("data.isBookmark").type(BOOLEAN).description("북마크 여부"),
                    fieldWithPath("data.bookmarkCount").type(NUMBER).description("북마크 수"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")))
            );
    }

    @Test
    @WithAccount
    @DisplayName("보호소 게시글 북마크 생성 테스트")
    void createShelterPostBookmarkTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/shelter-posts/{postId}/bookmark", 1L)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken()));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("create-shelter-post-bookmark",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                    parameterWithName("postId").description("게시글 아이디")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ))
            );
    }

    @Test
    @WithAccount
    @DisplayName("보호소 게시글 북마크 삭제 테스트")
    void deleteShelterPostBookmarkTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/shelter-posts/{postId}/bookmark", 1L)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken()));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(document("delete-shelter-post-bookmark",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                    parameterWithName("postId").description("게시글 아이디")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ))
            );
    }

}
