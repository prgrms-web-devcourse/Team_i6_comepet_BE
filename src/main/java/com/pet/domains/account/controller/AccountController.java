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
import com.pet.domains.account.dto.response.AccountReadResult;
import com.pet.domains.account.service.AccountService;
import com.pet.domains.auth.service.AuthenticationService;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import com.pet.domains.post.dto.serach.PostSearchParam;
import com.pet.domains.post.service.MissingPostService;
import com.pet.domains.post.service.ShelterPostService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    private final MissingPostService missingPostService;

    private final AuthenticationService authenticationService;

    private final ShelterPostService shelterPostService;

    @PostMapping(path = "/send-email", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendEmail(@RequestBody @Valid AccountEmailParam accountEmailParam) {
        accountService.sendEmail(accountEmailParam.getEmail());
    }

    @PatchMapping(path = "/send-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendPassword(@Valid @RequestBody AccountEmailParam param) {
        accountService.sendPassword(param.getEmail());
    }

    @PostMapping(path = "/verify-email", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Map<String, Long>> verifyEmail(@RequestBody @Valid AccountEmailCheck accountEmailCheck) {
        return ApiResponse.ok(
            Map.of("id", accountService.verifyEmail(accountEmailCheck.getEmail(), accountEmailCheck.getKey())));
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
        String email = accountLoginParam.getEmail();
        JwtAuthentication authentication = authenticationService.authenticate(email, accountLoginParam.getPassword());
        log.debug("login account email : {}", email);
        return ApiResponse.ok(AccountLoginResult.of(authentication.getAccountId(), authentication.getToken()));
    }

    @PostMapping(path = "/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@LoginAccount Account account) {
        log.debug("account id '{}' is logout", account.getId());
    }

    @GetMapping("/me")
    public ApiResponse<AccountReadResult> getAccount(@LoginAccount Account account) {
        return ApiResponse.ok(accountService.getAccount(account));
    }

    @PostMapping(path = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccount(
        @LoginAccount Account account,
        @RequestPart @Valid AccountUpdateParam param,
        @RequestPart(required = false) MultipartFile image
    ) {
        log.debug("회원 정보 수정 null check {}, {}, {}", account, param, image);
        accountService.updateAccount(account, param, image);
    }

    @GetMapping(path = "/me/areas", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AccountAreaReadResults> getAccountAreas(@LoginAccount Account account) {
        return ApiResponse.ok(accountService.getInterestArea(account));
    }

    @PutMapping(path = "/me/areas", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAccountArea(
        @LoginAccount Account account, @RequestBody @Valid AccountAreaUpdateParam accountAreaUpdateParam
    ) {
        accountService.updateArea(account, accountAreaUpdateParam);
    }

    @DeleteMapping(path = "/me/areas/{areaId}")
    public void deleteAccountArea(@LoginAccount Account account, @PathVariable Long areaId) {
        accountService.deleteArea(account, areaId);
    }

    @GetMapping(path = "/me/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AccountMissingPostPageResults> getAccountPosts(
        @LoginAccount Account account, Pageable pageable
    ) {
        return ApiResponse.ok(accountService.getAccountPosts(account.getId(), pageable));
    }

    @GetMapping(path = "/me/bookmarks", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AccountBookmarkPostPageResults> getAccountBookmarks(
        @LoginAccount Account account, @RequestParam(defaultValue = "missing") String status, Pageable pageable,
        @Valid PostSearchParam param
    ) {
        if (status.equals("shelter")) {
            return ApiResponse.ok(shelterPostService.getBookmarksThumbnailsByAccount(account, pageable, param));
        }
        return ApiResponse.ok(missingPostService.getBookmarksThumbnailsByAccount(account, pageable));
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@LoginAccount Account account) {
        accountService.deleteAccount(account);
    }

}
