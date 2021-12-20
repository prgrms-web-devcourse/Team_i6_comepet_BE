package com.pet.domains.post.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.LoginAccount;
import com.pet.domains.post.dto.response.ShelterPostPageResults;
import com.pet.domains.post.dto.response.ShelterPostReadResult;
import com.pet.domains.post.dto.serach.PostSearchParam;
import com.pet.domains.post.service.ShelterPostBookmarkService;
import com.pet.domains.post.service.ShelterPostService;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/shelter-posts")
@RequiredArgsConstructor
@RestController
public class ShelterPostController {

    private final ShelterPostService shelterPostService;

    private final ShelterPostBookmarkService shelterPostBookmarkService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ShelterPostPageResults> getShelterPosts(
        @LoginAccount Account account,
        @Valid PostSearchParam searchPostRequest,
        Pageable pageable
    ) {
        return ApiResponse.ok(getShelterPostPageResults(account, pageable, searchPostRequest));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ShelterPostReadResult> getShelterPost(@LoginAccount Account account, @PathVariable Long postId) {
        return ApiResponse.ok(getShelterPostReadResult(account, postId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/{postId}/bookmark")
    public void createShelterPostBookmark(@PathVariable Long postId, @LoginAccount Account account) {
        shelterPostBookmarkService.createPostBookmark(postId, account);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{postId}/bookmark")
    public void deleteShelterPostBookmark(@PathVariable Long postId, @LoginAccount Account account) {
        shelterPostBookmarkService.deletePostBookmark(postId, account);
    }

    private ShelterPostPageResults getShelterPostPageResults(
        Account account,
        Pageable pageable,
        PostSearchParam postSearchParam
    ) {
        if (Objects.nonNull(account)) {
            return shelterPostService.getShelterPostsPageWithAccount(account, pageable, postSearchParam);
        }
        return shelterPostService.getShelterPostsPage(pageable, postSearchParam);
    }

    private ShelterPostReadResult getShelterPostReadResult(Account account, Long postId) {
        if (Objects.nonNull(account)) {
            return shelterPostService.getShelterPostReadResultWithAccount(account, postId);
        }
        return shelterPostService.getShelterPostReadResult(postId);
    }
}
