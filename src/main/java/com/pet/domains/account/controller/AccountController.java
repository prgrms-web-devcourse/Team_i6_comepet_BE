package com.pet.domains.account.controller;

import com.pet.common.jwt.JwtMockToken;
import com.pet.common.response.ApiResponse;
import com.pet.domains.account.dto.request.AccountCreateParam;
import com.pet.domains.account.dto.request.AccountEmailParam;
import com.pet.domains.account.dto.request.AccountLonginParam;
import com.pet.domains.account.dto.request.AccountPasswordParam;
import com.pet.domains.account.dto.request.AccountUpdateParam;
import com.pet.domains.account.dto.response.AccountCreateResult;
import com.pet.domains.account.dto.response.AccountLoginResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    @PostMapping(path = "/verify-email", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyEmail(@RequestBody AccountEmailParam accountEmailParam) {
        log.info("verify email : {}", accountEmailParam.getEmail());
    }

    @PostMapping(path = "/sign-up",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AccountCreateResult> getCities(@RequestBody AccountCreateParam accountCreateParam) {
        String email = accountCreateParam.getEmail();
        String nickname = accountCreateParam.getNickname();
        log.info("sign up account info : {}, {}", email, nickname);
        return ApiResponse.ok(AccountCreateResult.of(1L, JwtMockToken.MOCK_TOKEN));
    }

    @PostMapping(path = "/login",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AccountLoginResult> login(@RequestBody AccountLonginParam accountLoginParam) {
        String email = accountLoginParam.getEmail();
        log.info("sign up account info : {}", email);
        return ApiResponse.ok(AccountLoginResult.of(1L, JwtMockToken.MOCK_TOKEN));
    }

    @PostMapping(path = "/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        log.info("who is logout");
    }

    @PatchMapping(path = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccount(@RequestBody AccountUpdateParam accountUpdateParam) {
        log.info("{} id account update nickname : {} ", accountUpdateParam.getId(), accountUpdateParam.getNickname());
    }

    @PatchMapping(path = "/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccountByPassword(@RequestBody AccountPasswordParam accountPasswordParam) {
        log.info("success change password");
    }

}
