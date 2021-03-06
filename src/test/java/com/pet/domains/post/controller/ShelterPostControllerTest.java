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

@DisplayName("????????? ?????? ????????? ???????????? ?????????")
class ShelterPostControllerTest extends BaseDocumentationTest {

    @Test
    @WithAccount
    @DisplayName("????????? ????????? ????????? ?????? ?????????")
    void getShelterPostsTest() throws Exception {
        // given
        var results = ShelterPostPageResults.of(List.of(
            ShelterPostPageResults.ShelterPost.builder()
                .id(1L)
                .city("???????????????")
                .town("?????????")
                .age(2018L)
                .sex(SexType.UNKNOWN)
                .thumbnail("http://www.animal.go.kr/files/shelter/2021/11/202112140012452_s.jpg")
                .animal("???")
                .animalKindName("????????????")
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
                    parameterWithName("page").description("????????? ??????"),
                    parameterWithName("size").description("????????? ??????"),
                    parameterWithName("sort").description("??????, ex) id,[desc]")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.shelters").type(ARRAY).description("????????? ????????? ?????????"),
                    fieldWithPath("data.shelters[].id").type(NUMBER).description("????????? id"),
                    fieldWithPath("data.shelters[].city").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.shelters[].town").type(STRING).description("????????? ??????"),
                    fieldWithPath("data.shelters[].age").type(NUMBER).description("?????? ??????"),
                    fieldWithPath("data.shelters[].sex").type(STRING).description("<<sexType,?????? ??????>>>>"),
                    fieldWithPath("data.shelters[].thumbnail").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.shelters[].animal").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.shelters[].animalKindName").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.shelters[].foundDate").type(STRING).description("?????????"),
                    fieldWithPath("data.shelters[].isBookmark").type(BOOLEAN).description("????????? ??????"),
                    fieldWithPath("data.shelters[].bookmarkCount").type(NUMBER).description("????????? ???"),
                    fieldWithPath("data.totalElements").type(NUMBER).description("?????? ????????????"),
                    fieldWithPath("data.last").type(BOOLEAN).description("????????? ????????? ??????"),
                    fieldWithPath("data.size").type(NUMBER).description("????????? ??????"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")))
            );
    }

    @Test
    @WithAccount
    @DisplayName("????????? ????????? ?????? ?????? ?????????")
    void getShelterPostTest() throws Exception {
        // given
        var results = ShelterPostReadResult.builder()
            .id(1L)
            .city("???????????????")
            .town("?????????")
            .age(2018L)
            .image("http://www.animal.go.kr/files/shelter/2021/11/202112140012452_s.jpg")
            .animal("???")
            .animalKindName("????????????")
            .foundDate(LocalDate.of(2021, 12, 11))
            .isBookmark(true)
            .bookmarkCount(13L)
            .color("??????")
            .startDate(LocalDate.of(2021, 10, 1))
            .endDate(LocalDate.of(2021, 12, 1))
            .feature("????????????")
            .foundPlace("???????????? ????????? ?????? ????????? 3-15")
            .managerTelNumber("055-749-6134")
            .neutered("N")
            .shelterName("??????????????????????????????")
            .noticeNumber("??????-??????-2021-00262")
            .sex("MALE")
            .status("?????????")
            .shelterPlace("???????????? ????????? ????????? ????????? 8-4  ????????? ????????????????????????")
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
                    parameterWithName("postId").description("????????? ?????????")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token - optional").optional()
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.id").type(NUMBER).description("????????? id"),
                    fieldWithPath("data.age").type(NUMBER).description("?????? ??????"),
                    fieldWithPath("data.city").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.town").type(STRING).description("????????? ??????"),
                    fieldWithPath("data.shelterName").type(STRING).description("????????? ??????"),
                    fieldWithPath("data.shelterTelNumber").type(STRING).description("????????? ????????????"),
                    fieldWithPath("data.shelterPlace").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.color").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.image").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.foundDate").type(STRING).description("?????????"),
                    fieldWithPath("data.foundPlace").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.animal").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.animalKindName").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.neutered").type(STRING).description("<<neuteredType,????????? ??????>>"),
                    fieldWithPath("data.startDate").type(STRING).description("?????? ?????????"),
                    fieldWithPath("data.endDate").type(STRING).description("?????? ?????????"),
                    fieldWithPath("data.noticeNumber").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.managerTelNumber").type(STRING).description("????????? ?????????"),
                    fieldWithPath("data.status").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.sex").type(STRING).description("<<sexType,?????? ??????>>"),
                    fieldWithPath("data.feature").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.weight").type(NUMBER).description("?????? ??????"),
                    fieldWithPath("data.isBookmark").type(BOOLEAN).description("????????? ??????"),
                    fieldWithPath("data.bookmarkCount").type(NUMBER).description("????????? ???"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")))
            );
    }

    @Test
    @WithAccount
    @DisplayName("????????? ????????? ????????? ?????? ?????????")
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
                    parameterWithName("postId").description("????????? ?????????")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ))
            );
    }

    @Test
    @WithAccount
    @DisplayName("????????? ????????? ????????? ?????? ?????????")
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
                    parameterWithName("postId").description("????????? ?????????")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ))
            );
    }

}
