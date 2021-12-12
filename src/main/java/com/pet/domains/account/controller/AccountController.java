package com.pet.domains.account.controller;

import static java.util.stream.Collectors.*;
import com.pet.common.jwt.JwtAuthentication;
import com.pet.common.response.ApiResponse;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.LoginAccount;
import com.pet.domains.account.dto.request.AccountAreaUpdateParam;
import com.pet.domains.account.dto.request.AccountEmailParam;
import com.pet.domains.account.dto.request.AccountSignUpParam;
import com.pet.domains.account.dto.request.AccountEmailCheck;
import com.pet.domains.account.dto.request.AccountLonginParam;
import com.pet.domains.account.dto.request.AccountUpdateParam;
import com.pet.domains.account.dto.response.AccountAreaReadResults;
import com.pet.domains.account.dto.response.AccountBookmarkPostPageResults;
import com.pet.domains.account.dto.response.AccountCreateResult;
import com.pet.domains.account.dto.response.AccountLoginResult;
import com.pet.domains.account.dto.response.AccountMissingPostPageResults;
import com.pet.domains.account.service.AccountService;
import com.pet.domains.auth.service.AuthenticationService;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.LongStream;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    private final AuthenticationService authenticationService;

    @PostMapping(path = "/send-email", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendEmail(@RequestBody @Valid AccountEmailParam accountEmailParam) {
        accountService.sendEmail(accountEmailParam.getEmail());
    }

    @PostMapping(path = "/verify-email", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyEmail(@RequestBody @Valid AccountEmailCheck accountEmailCheck) {
        accountService.verifyEmail(accountEmailCheck.getEmail(), accountEmailCheck.getKey());
    }

    @PostMapping(path = "/sign-up",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AccountCreateResult> signUp(@RequestBody @Valid AccountSignUpParam accountSignUpParam) {
        Long id = accountService.signUp(accountSignUpParam);
        return ApiResponse.ok(AccountCreateResult.of(id, authenticationService.authenticate(
            accountSignUpParam.getEmail(), accountSignUpParam.getPassword()).getToken()));
    }

    @PostMapping(path = "/login",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AccountLoginResult> login(@RequestBody AccountLonginParam accountLoginParam) {
        // todo: 서버에서 확인하고 삭제할 것
        log.info("request password : {} ", accountLoginParam.getPassword());
        String email = accountLoginParam.getEmail();
        log.info("login account email : {}", email);
        JwtAuthentication authentication = authenticationService.authenticate(email, accountLoginParam.getPassword());
        return ApiResponse.ok(AccountLoginResult.of(authentication.getAccountId(), authentication.getToken()));
    }

    @PostMapping(path = "/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@LoginAccount Account account) {
        log.info("account id '{}' is logout", account.getId());
    }

    @PatchMapping(path = "/me/image", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccountImage(@LoginAccount Account account) {
        /** 이미지 서비스 머지 후 수정*/
        accountService.updateImage(account, "temp");
    }

    @PatchMapping(path = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccount(@LoginAccount Account account,
        @Valid @RequestBody AccountUpdateParam accountUpdateParam
    ) {
        accountService.updateAccount(account, accountUpdateParam);
    }

    @GetMapping(path = "/me/areas", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AccountAreaReadResults> getAccountAreas() {
        return ApiResponse.ok(AccountAreaReadResults.of(
            List.of(
                AccountAreaReadResults.Area.of(1L, "서울특별시", 1L, "강남구", true),
                AccountAreaReadResults.Area.of(1L, "서울특별시", 2L, "강동구", false)
            )
        ));
    }

    @PutMapping(path = "/me/areas", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccountArea(
        @LoginAccount Account account, @RequestBody AccountAreaUpdateParam accountAreaUpdateParam
    ) {
        accountService.updateArea(account, accountAreaUpdateParam);
    }

    @GetMapping(path = "/me/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AccountMissingPostPageResults> getAccountPosts() {
        return ApiResponse.ok(
            AccountMissingPostPageResults.of(
                LongStream.range(1, 9)
                    .mapToObj(index -> AccountMissingPostPageResults.Post.of(
                        index,
                        "서울특별시",
                        "도봉구",
                        "토이푸들",
                        Status.DETECTION,
                        LocalDate.of(2021, 11, 3),
                        SexType.FEMALE,
                        true,
                        2,
                        List.of(
                            AccountMissingPostPageResults.Post.Tag.of(123L, "암컷"),
                            AccountMissingPostPageResults.Post.Tag.of(431L, "5살"),
                            AccountMissingPostPageResults.Post.Tag.of(256L, "4kg"),
                            AccountMissingPostPageResults.Post.Tag.of(1246L, "사람 좋아함")
                        ),
                        "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png"
                    ))
                    .collect(toList()), 8, true, 1)
        );

    }

    @GetMapping(path = "/me/bookmarks", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AccountBookmarkPostPageResults> getAccountBookmarks(
        @RequestParam(defaultValue = "missing") String status
    ) {
        /** RequestParam 우선 하드코딩 */
        if (status.equals("shelter")) {
            return getAccountShelterBookmarkPosts();
        }

        if (status.equals("missing")) {
            return getAccountMissingBookmarkPosts();
        }

        throw new IllegalArgumentException();
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount() {
        log.info("success deleted account");
    }

    private ApiResponse<AccountBookmarkPostPageResults> getAccountShelterBookmarkPosts() {
        return ApiResponse.ok(
            AccountBookmarkPostPageResults.of(
                LongStream.range(1, 8)
                    .mapToObj(index -> AccountBookmarkPostPageResults.Post.of(
                        index,
                        "포메라니안",
                        SexType.FEMALE,
                        "경상남도 진주시 집현면 신당길 207번길 22 (집현면, 지엽농업개발시설)",
                        LocalDate.of(2021, 4, 11),
                        "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png",
                        5))
                    .collect(toList()), 15, false, 8
            )
        );
    }

    private ApiResponse<AccountBookmarkPostPageResults> getAccountMissingBookmarkPosts() {
        return ApiResponse.ok(
            AccountBookmarkPostPageResults.of(
                LongStream.range(1, 8)
                    .mapToObj(index -> AccountBookmarkPostPageResults.Post.of(
                        index,
                        "포메라니안",
                        SexType.MALE,
                        "인천 광역시 미추홀구 용현 1동",
                        LocalDate.of(2021, 6, 13),
                        "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png",
                        3))
                    .collect(toList()), 15, false, 8
            )
        );
    }
}
