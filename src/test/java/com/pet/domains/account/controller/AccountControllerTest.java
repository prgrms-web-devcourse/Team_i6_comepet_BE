package com.pet.domains.account.controller;

import static com.pet.domains.docs.utils.ApiDocumentUtils.getDocumentRequest;
import static com.pet.domains.docs.utils.ApiDocumentUtils.getDocumentResponse;
import static java.util.stream.Collectors.toList;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.pet.common.jwt.JwtAuthentication;
import com.pet.domains.account.WithAccount;
import com.pet.domains.account.domain.SignEmail;
import com.pet.domains.account.dto.request.AccountAreaUpdateParam;
import com.pet.domains.account.dto.request.AccountEmailCheck;
import com.pet.domains.account.dto.request.AccountEmailParam;
import com.pet.domains.account.dto.request.AccountLonginParam;
import com.pet.domains.account.dto.request.AccountSignUpParam;
import com.pet.domains.account.dto.request.AccountUpdateParam;
import com.pet.domains.account.dto.response.AccountAreaReadResults;
import com.pet.domains.account.dto.response.AccountBookmarkPostPageResults;
import com.pet.domains.account.dto.response.AccountMissingPostPageResults;
import com.pet.domains.account.dto.response.AccountReadResult;
import com.pet.domains.docs.BaseDocumentationTest;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.LongStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("?????? ???????????? ?????????")
class AccountControllerTest extends BaseDocumentationTest {

    @Test
    @DisplayName("????????? ?????? ?????? ?????? ?????????")
    void emailVerifyTest() throws Exception {
        // given
        AccountEmailCheck param = new AccountEmailCheck("tester@email.com", "131231231234123");
        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/verify-email")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(param)));

        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("verify-email",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestFields(
                    fieldWithPath("email").type(STRING).description("?????????"),
                    fieldWithPath("key").type(STRING).description("????????? ????????????")
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.id").type(NUMBER).description("????????? ?????? id"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")
                ))
            );
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ?????? ?????????")
    void sendNewPasswordEmail() throws Exception {
        // given
        AccountEmailParam param = new AccountEmailParam("tester-user@email.com");
        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/send-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(param)));

        resultActions
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(document("send-newPassword",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                    fieldWithPath("email").type(STRING).description("?????????")
                )
            ));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    void signUpTest() throws Exception {
        // given
        AccountSignUpParam param = new AccountSignUpParam(
            "test", "test@gmail.com", "1234123a!", "1234123a!", 14L);

        given(authenticationService.authenticate(param.getEmail(), param.getPassword()))
            .willReturn(new JwtAuthentication("mock-token", 1L));

        given(mock(SignEmail.class).isVerifyEmail("test@gmail.com")).willReturn(true);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(param)));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("sign-up",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestFields(
                    fieldWithPath("nickname").type(STRING).description("?????????"),
                    fieldWithPath("email").type(STRING).description("?????????"),
                    fieldWithPath("password").type(STRING).description("????????????"),
                    fieldWithPath("passwordCheck").type(STRING).description("???????????? ??????"),
                    fieldWithPath("verifiedId").type(NUMBER).description("?????? id"),
                    fieldWithPath("file").type(OBJECT).description("????????? ?????????").optional()
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.id").type(NUMBER).description("?????? id"),
                    fieldWithPath("data.token").type(STRING).description("??????"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")
                ))
            );
    }

    @Test
    @WithAnonymousUser
    @DisplayName("????????? ?????? ?????????")
    void loginTest() throws Exception {
        // given
        AccountLonginParam param = new AccountLonginParam("tester@email.com", "12345678a!");

        given(authenticationService.authenticate(param.getEmail(), param.getPassword()))
            .willReturn(new JwtAuthentication("mock-token", 1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/login")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(param)));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("login",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.id").type(NUMBER).description("?????? id"),
                    fieldWithPath("data.token").type(STRING).description("??????"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")
                ))
            );
    }

    @Test
    @WithAccount
    @DisplayName("???????????? ?????????")
    void logoutTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/logout")
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken()));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(document("logout",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ))
            );
    }

    @Test
    @WithAccount
    @DisplayName("?????? ?????? ??????")
    void getAccount() throws Exception {
        // given
        given(accountService.getAccount(any()))
            .willReturn(new AccountReadResult(1L, "nickname", "tester@email.com", "image.png"));
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/me")
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken())
            .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-account",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.id").type(NUMBER).description("?????? id"),
                    fieldWithPath("data.nickname").type(STRING).description("?????????"),
                    fieldWithPath("data.email").type(STRING).description("?????????"),
                    fieldWithPath("data.image").type(STRING).description("?????????"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")
                ))
            );
    }

    @Test
    @WithAccount
    @DisplayName("?????? ?????? ?????? ?????????")
    void updateAccountTest() throws Exception {
        // given
        MockMultipartFile param =
            new MockMultipartFile("param", "", "application/json",
                objectMapper.writeValueAsString(
                    new AccountUpdateParam("updateNickname", "12341234a!", "12341234a!")).getBytes(
                    StandardCharsets.UTF_8)
            );
        MockMultipartFile profileImage =
            new MockMultipartFile("image", "",
                "multipart/form-data", "comepet.jpg".getBytes());

        // when
        ResultActions resultActions = mockMvc.perform(multipart("/api/v1/me")
            .file(profileImage)
            .file(param)
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken())
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(document("update-account",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.MULTIPART_FORM_DATA_VALUE)
                ),
                requestParts(
                    partWithName("image").description("?????? ?????????"),
                    partWithName("param").description("?????? ?????? ?????? ?????????")
                ))
            );
    }

    @Test
    @WithAccount
    @DisplayName("????????? ?????? ?????? ?????? ?????????")
    void getAccountAreaTest() throws Exception {
        // given
        AccountAreaReadResults result = AccountAreaReadResults.of(
            List.of(
                AccountAreaReadResults.Area.of(312L, 1L, "???????????????", 1L, "?????????", true),
                AccountAreaReadResults.Area.of(213L, 1L, "???????????????", 2L, "?????????", false)
            ), true
        );
        given(accountService.getInterestArea(any())).willReturn(result);
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/me/areas")
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken()));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-account-areas",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.areas").type(ARRAY).description("??????"),
                    fieldWithPath("data.areas[0].id").type(NUMBER).description("?????? ?????? id"),
                    fieldWithPath("data.areas[0].cityId").type(NUMBER).description("?????? id"),
                    fieldWithPath("data.areas[0].cityName").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.areas[0].townId").type(NUMBER).description("????????? id"),
                    fieldWithPath("data.areas[0].townName").type(STRING).description("????????? ??????"),
                    fieldWithPath("data.areas[0].defaultArea").type(BOOLEAN).description("????????? ?????? ??????"),
                    fieldWithPath("data.checked").type(BOOLEAN).description("?????? ?????? ??????"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")
                ))
            );

    }

    @Test
    @WithAccount
    @DisplayName("?????? ?????? ?????? ?????????")
    void updateAccountAreaTest() throws Exception {
        // given
        AccountAreaUpdateParam param = new AccountAreaUpdateParam(
            List.of(
                new AccountAreaUpdateParam.Area(1L, true),
                new AccountAreaUpdateParam.Area(2L, false)
            ), true
        );
        // when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/me/areas")
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(param)));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(document("update-account-areas",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                requestFields(
                    fieldWithPath("areas").type(ARRAY).description("??????"),
                    fieldWithPath("areas[0].townId").type(NUMBER).description("????????? id"),
                    fieldWithPath("areas[0].defaultArea").type(BOOLEAN).description("????????? ?????? ??????"),
                    fieldWithPath("notification").type(BOOLEAN).description("?????? ??????")
                ))
            );
    }

    @Test
    @WithAccount
    @DisplayName("?????? ????????? ??????")
    void getAccountMissingPostsTest() throws Exception {
        // given
        given(accountService.getAccountPosts(any(), any())).willReturn(AccountMissingPostPageResults.of(
            LongStream.range(1, 9)
                .mapToObj(index -> AccountMissingPostPageResults.Post.of(
                    index,
                    "???????????????",
                    "?????????",
                    "????????????",
                    Status.DETECTION,
                    LocalDate.of(2021, 11, 3),
                    SexType.FEMALE,
                    2,
                    List.of(
                        AccountMissingPostPageResults.Post.Tag.of(123L, "??????"),
                        AccountMissingPostPageResults.Post.Tag.of(431L, "5???"),
                        AccountMissingPostPageResults.Post.Tag.of(256L, "4kg"),
                        AccountMissingPostPageResults.Post.Tag.of(1246L, "?????? ?????????")
                    ),
                    "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png"
                ))
                .collect(toList()), 8, true, 1));
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/me/posts"));
        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-account-missing-posts",
                getDocumentRequest(),
                getDocumentResponse(),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.posts").type(ARRAY).description("?????????"),
                    fieldWithPath("data.posts[0].id").type(NUMBER).description("????????? id"),
                    fieldWithPath("data.posts[0].city").type(STRING).description("?????? ??????"),
                    fieldWithPath("data.posts[0].town").type(STRING).description("????????? ??????"),
                    fieldWithPath("data.posts[0].animalKind").type(STRING).description("??????"),
                    fieldWithPath("data.posts[0].status").type(STRING).description("????????? ??????"),
                    fieldWithPath("data.posts[0].date").type(STRING).description("????????? ?????? ??????"),
                    fieldWithPath("data.posts[0].sex").type(STRING).description("??????"),
                    fieldWithPath("data.posts[0].bookmarkCount").type(NUMBER).description("????????? ???"),
                    fieldWithPath("data.posts[0].postTags").type(ARRAY).description("????????? ??????"),
                    fieldWithPath("data.posts[0].postTags[0].id").type(NUMBER).description("????????? ?????? id"),
                    fieldWithPath("data.posts[0].postTags[0].name").type(STRING).description("????????? ?????? ??????"),
                    fieldWithPath("data.posts[0].thumbnail").type(STRING).description("????????? ?????????"),
                    fieldWithPath("data.totalElements").type(NUMBER).description("??? ????????? ???"),
                    fieldWithPath("data.last").type(BOOLEAN).description("????????? ????????? ??????"),
                    fieldWithPath("data.size").type(NUMBER).description("????????? ??????"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")
                ))
            );

    }

    @Test
    @WithAccount
    @DisplayName("?????? ?????????(??????/??????) ??????")
    void getAccountMissingBookmarkPostTest() throws Exception {
        // given
        given(missingPostService.getBookmarksThumbnailsByAccount(any(), any())).willReturn(
            AccountBookmarkPostPageResults.of(
                LongStream.range(1, 8)
                    .mapToObj(index -> AccountBookmarkPostPageResults.Post.of(
                        index,
                        "???????????????",
                        SexType.MALE,
                        "?????? ?????????",
                        "???????????? ?????? 1???",
                        LocalDate.of(2021, 6, 13),
                        "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png",
                        3))
                    .collect(toList()), 15, false, 8
            )
        );
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/me/bookmarks?status=missing"));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-account-missing-bookmark-posts",
                getDocumentRequest(),
                getDocumentResponse(),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestParameters(
                    parameterWithName("status").description("????????? ??????")
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.posts").type(ARRAY).description("?????????"),
                    fieldWithPath("data.posts[0].id").type(NUMBER).description("????????? id"),
                    fieldWithPath("data.posts[0].animalKind").type(STRING).description("??????"),
                    fieldWithPath("data.posts[0].sexType").type(STRING).description("??????"),
                    fieldWithPath("data.posts[0].city").type(STRING).description("??????"),
                    fieldWithPath("data.posts[0].town").type(STRING).description("?????????"),
                    fieldWithPath("data.posts[0].createdAt").type(STRING).description("????????? ?????????"),
                    fieldWithPath("data.posts[0].sexType").type(STRING).description("??????"),
                    fieldWithPath("data.posts[0].thumbnail").type(STRING).description("????????? ?????????"),
                    fieldWithPath("data.posts[0].bookmarkCount").type(NUMBER).description("????????? ???"),
                    fieldWithPath("data.totalElements").type(NUMBER).description("??? ????????? ???"),
                    fieldWithPath("data.last").type(BOOLEAN).description("????????? ????????? ??????"),
                    fieldWithPath("data.size").type(NUMBER).description("????????? ??????"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")
                ))
            );

    }

    @Test
    @WithAccount
    @DisplayName("?????? ?????????(?????????) ??????")
    void getAccountShelterBookmarkPostTest() throws Exception {
        // given
        given(shelterPostService.getBookmarksThumbnailsByAccount(any(), any())).willReturn(
            AccountBookmarkPostPageResults.of(
                LongStream.range(1, 8)
                    .mapToObj(index -> AccountBookmarkPostPageResults.Post.of(
                        index,
                        "???????????????",
                        SexType.FEMALE,
                        "???????????? ?????????",
                        "????????? ????????? 207?????? 22 (?????????, ????????????????????????)",
                        LocalDate.of(2021, 4, 11),
                        "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png",
                        5))
                    .collect(toList()), 15, false, 8
            )
        );
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/me/bookmarks?status=shelter"));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-account-shelter-bookmark-posts",
                getDocumentRequest(),
                getDocumentResponse(),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestParameters(
                    parameterWithName("status").description("????????? ??????")
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("?????? ?????????"),
                    fieldWithPath("data.posts").type(ARRAY).description("?????????"),
                    fieldWithPath("data.posts[0].id").type(NUMBER).description("????????? id"),
                    fieldWithPath("data.posts[0].animalKind").type(STRING).description("??????"),
                    fieldWithPath("data.posts[0].sexType").type(STRING).description("??????"),
                    fieldWithPath("data.posts[0].city").type(STRING).description("??????"),
                    fieldWithPath("data.posts[0].town").type(STRING).description("?????????"),
                    fieldWithPath("data.posts[0].createdAt").type(STRING).description("????????? ?????????"),
                    fieldWithPath("data.posts[0].sexType").type(STRING).description("??????"),
                    fieldWithPath("data.posts[0].thumbnail").type(STRING).description("????????? ?????????"),
                    fieldWithPath("data.posts[0].bookmarkCount").type(NUMBER).description("????????? ???"),
                    fieldWithPath("data.totalElements").type(NUMBER).description("??? ????????? ???"),
                    fieldWithPath("data.last").type(BOOLEAN).description("????????? ????????? ??????"),
                    fieldWithPath("data.size").type(NUMBER).description("????????? ??????"),
                    fieldWithPath("serverDateTime").type(STRING).description("?????? ?????? ??????")
                ))
            );

    }

    @Test
    @WithAccount
    @DisplayName("?????? ?????? ?????????")
    void deleteAccount() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/me"));
        // then
        resultActions
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(document("delete-account",
                getDocumentRequest(),
                getDocumentResponse()
            ));
    }

}
