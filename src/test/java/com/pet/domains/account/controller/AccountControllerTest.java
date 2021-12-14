package com.pet.domains.account.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.pet.common.jwt.JwtAuthentication;
import com.pet.common.jwt.JwtMockToken;
import com.pet.domains.account.WithAccount;
import com.pet.domains.account.domain.SignEmail;
import com.pet.domains.account.dto.request.AccountAreaUpdateParam;
import com.pet.domains.account.dto.request.AccountSignUpParam;
import com.pet.domains.account.dto.request.AccountEmailCheck;
import com.pet.domains.account.dto.request.AccountLonginParam;
import com.pet.domains.account.dto.request.AccountUpdateParam;
import com.pet.domains.docs.BaseDocumentationTest;
import java.util.List;
import lombok.With;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("회원 컨트롤러 테스트")
class AccountControllerTest extends BaseDocumentationTest {

    @Test
    @DisplayName("이메일 인증 요청 성공 테스트")
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
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.id").type(NUMBER).description("이메일 인증 id"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")
                ))
            );
    }

    @Test
    @DisplayName("회원 가입 요청 성공 테스트")
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
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestFields(
                    fieldWithPath("nickname").type(STRING).description("닉네임"),
                    fieldWithPath("email").type(STRING).description("이메일"),
                    fieldWithPath("password").type(STRING).description("비밀번호"),
                    fieldWithPath("passwordCheck").type(STRING).description("비밀번호 확인"),
                    fieldWithPath("verifiedId").type(NUMBER).description("검증 id"),
                    fieldWithPath("file").type(OBJECT).description("프로필 이미지").optional()
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.id").type(NUMBER).description("회원 id"),
                    fieldWithPath("data.token").type(STRING).description("토큰"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")
                ))
            );
    }

    @Test
    @WithAnonymousUser
    @DisplayName("로그인 성공 테스트")
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
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.id").type(NUMBER).description("회원 id"),
                    fieldWithPath("data.token").type(STRING).description("토큰"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")
                ))
            );
    }

    @Test
    @WithAccount
    @DisplayName("로그아웃 테스트")
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
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ))
            );
    }

    @Test
    @WithAccount
    @DisplayName("회원 정보 수정 테스트")
    void updateAccountTest() throws Exception {
        // given
        AccountUpdateParam param = new AccountUpdateParam("updateNickname", "12341234a!", "12341234a!");
        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/me")
            .header(HttpHeaders.AUTHORIZATION, getAuthenticationToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(param)));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(document("update-account",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestFields(
                    fieldWithPath("nickname").type(STRING).description("닉네임").optional(),
                    fieldWithPath("newPassword").type(STRING).description("수정 비밀번호").optional(),
                    fieldWithPath("newPasswordCheck").type(STRING).description("수정 비밀번호 확인").optional()
                ))
            );
    }

    @Test
    @DisplayName("회원의 관심 지역 조회 테스트")
    void getAccountAreaTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/me/areas")
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-account-areas",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.areas").type(ARRAY).description("시도"),
                    fieldWithPath("data.areas[0].cityId").type(NUMBER).description("시도 id"),
                    fieldWithPath("data.areas[0].cityName").type(STRING).description("시도 이름"),
                    fieldWithPath("data.areas[0].townId").type(NUMBER).description("시군구 id"),
                    fieldWithPath("data.areas[0].townName").type(STRING).description("시군구 이름"),
                    fieldWithPath("data.areas[0].defaultArea").type(BOOLEAN).description("디폴트 지역 여부"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")
                ))
            );

    }

    @Test
    @WithAccount
    @DisplayName("관심 지역 설정 테스트")
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
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE),
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                requestFields(
                    fieldWithPath("areas").type(ARRAY).description("시도"),
                    fieldWithPath("areas[0].townId").type(NUMBER).description("시군구 id"),
                    fieldWithPath("areas[0].defaultArea").type(BOOLEAN).description("디폴트 지역 여부"),
                    fieldWithPath("notification").type(BOOLEAN).description("알림 여부")
                ))
            );
    }

    @Test
    @DisplayName("회원 게시물 조회")
    void getAccountMissingPostsTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/me/posts")
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-account-missing-posts",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.posts").type(ARRAY).description("게시물"),
                    fieldWithPath("data.posts[0].id").type(NUMBER).description("게시물 id"),
                    fieldWithPath("data.posts[0].city").type(STRING).description("시도 이름"),
                    fieldWithPath("data.posts[0].town").type(STRING).description("시군구 이름"),
                    fieldWithPath("data.posts[0].animalKind").type(STRING).description("품종"),
                    fieldWithPath("data.posts[0].status").type(STRING).description("게시물 상태"),
                    fieldWithPath("data.posts[0].date").type(STRING).description("게시물 등록 날짜"),
                    fieldWithPath("data.posts[0].sex").type(STRING).description("성별"),
                    fieldWithPath("data.posts[0].isBookmark").type(BOOLEAN).description("북마크 여부"),
                    fieldWithPath("data.posts[0].bookmarkCount").type(NUMBER).description("북마크 수"),
                    fieldWithPath("data.posts[0].postTags").type(ARRAY).description("게시물 태그"),
                    fieldWithPath("data.posts[0].postTags[0].id").type(NUMBER).description("게시물 태그 id"),
                    fieldWithPath("data.posts[0].postTags[0].name").type(STRING).description("게시물 태그 이름"),
                    fieldWithPath("data.posts[0].thumbnail").type(STRING).description("게시물 썸네일"),
                    fieldWithPath("data.totalElements").type(NUMBER).description("총 게시물 수"),
                    fieldWithPath("data.last").type(BOOLEAN).description("마지막 페이지 여부"),
                    fieldWithPath("data.size").type(NUMBER).description("페이지 크기"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")
                ))
            );

    }

    @Test
    @DisplayName("회원 북마크(실종/보호) 조회")
    void getAccountMissingBookmarkPostTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/me/bookmarks?status=missing")
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-account-missing-bookmark-posts",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestParameters(
                    parameterWithName("status").description("게시물 종류")
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.posts").type(ARRAY).description("게시물"),
                    fieldWithPath("data.posts[0].id").type(NUMBER).description("게시물 id"),
                    fieldWithPath("data.posts[0].animalKind").type(STRING).description("품종"),
                    fieldWithPath("data.posts[0].sexType").type(STRING).description("성별"),
                    fieldWithPath("data.posts[0].place").type(STRING).description("위치"),
                    fieldWithPath("data.posts[0].createdAt").type(STRING).description("게시물 생성일"),
                    fieldWithPath("data.posts[0].sexType").type(STRING).description("성별"),
                    fieldWithPath("data.posts[0].thumbnail").type(STRING).description("게시물 썸네일"),
                    fieldWithPath("data.posts[0].bookmarkCount").type(NUMBER).description("북마크 수"),
                    fieldWithPath("data.totalElements").type(NUMBER).description("총 게시물 수"),
                    fieldWithPath("data.last").type(BOOLEAN).description("마지막 페이지 여부"),
                    fieldWithPath("data.size").type(NUMBER).description("페이지 크기"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")
                ))
            );

    }

    @Test
    @DisplayName("회원 북마크(보호소) 조회")
    void getAccountShelterBookmarkPostTest() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/me/bookmarks?status=shelter")
            .header(HttpHeaders.AUTHORIZATION, JwtMockToken.MOCK_TOKEN));

        // then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("get-account-shelter-bookmark-posts",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                ),
                requestParameters(
                    parameterWithName("status").description("게시물 종류")
                ),
                responseFields(
                    fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                    fieldWithPath("data.posts").type(ARRAY).description("게시물"),
                    fieldWithPath("data.posts[0].id").type(NUMBER).description("게시물 id"),
                    fieldWithPath("data.posts[0].animalKind").type(STRING).description("품종"),
                    fieldWithPath("data.posts[0].sexType").type(STRING).description("성별"),
                    fieldWithPath("data.posts[0].place").type(STRING).description("위치"),
                    fieldWithPath("data.posts[0].createdAt").type(STRING).description("게시물 생성일"),
                    fieldWithPath("data.posts[0].sexType").type(STRING).description("성별"),
                    fieldWithPath("data.posts[0].thumbnail").type(STRING).description("게시물 썸네일"),
                    fieldWithPath("data.posts[0].bookmarkCount").type(NUMBER).description("북마크 수"),
                    fieldWithPath("data.totalElements").type(NUMBER).description("총 게시물 수"),
                    fieldWithPath("data.last").type(BOOLEAN).description("마지막 페이지 여부"),
                    fieldWithPath("data.size").type(NUMBER).description("페이지 크기"),
                    fieldWithPath("serverDateTime").type(STRING).description("서버 응답 시간")
                ))
            );

    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void deleteAccount() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/me"));
        // then
        resultActions
            .andDo(print())
            .andExpect(status().isNoContent())
            .andDo(document("delete-account",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            ));
    }

}
