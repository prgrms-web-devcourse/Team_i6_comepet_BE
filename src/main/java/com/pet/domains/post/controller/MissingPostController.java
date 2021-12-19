package com.pet.domains.post.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.LoginAccount;
import com.pet.domains.comment.dto.response.CommentPageResults;
import com.pet.domains.comment.service.CommentService;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import com.pet.domains.post.dto.request.MissingPostUpdateParam;
import com.pet.domains.post.dto.response.MissingPostReadResult;
import com.pet.domains.post.dto.response.MissingPostReadResults;
import com.pet.domains.post.dto.serach.PostSearchParam;
import com.pet.domains.post.service.MissingPostBookmarkService;
import com.pet.domains.post.service.MissingPostService;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/missing-posts")
public class MissingPostController {

    private static final String RETURN_KEY = "id";

    private final MissingPostService missingPostService;

    private final MissingPostBookmarkService missingPostBookmarkService;

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Map<String, Long>> createMissingPost(
        @RequestPart(value = "images", required = false) List<MultipartFile> images,
        @RequestPart(value = "param") @Valid MissingPostCreateParam missingPostCreateParam,
        @LoginAccount Account account
    ) {
        return ApiResponse.ok(
            Map.of(RETURN_KEY, missingPostService.createMissingPost(missingPostCreateParam, images, account)));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<MissingPostReadResults> getMissingPosts(
        @LoginAccount Account account,
        Pageable pageable,
        @Valid PostSearchParam searchParam
    ) {
        log.info("실종/보호 게시물 리스트 조회");
        if (Objects.nonNull(account)) {
            return ApiResponse.ok(missingPostService.getMissingPostsPageWithAccount(account, pageable, searchParam));
        }
        return ApiResponse.ok(missingPostService.getMissingPostsPage(pageable, searchParam));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<MissingPostReadResult> getMissingPost(@LoginAccount Account account, @PathVariable Long postId) {
        log.info("실종/보호 게시물 단건 조회");
        if (Objects.nonNull(account)) {
            return ApiResponse.ok(missingPostService.getMissingPostOneWithAccount(account, postId));
        }
        return ApiResponse.ok(missingPostService.getMissingPostOne(postId));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Map<String, Long>> updateMissingPost(
        @PathVariable Long postId,
        @RequestPart(value = "images", required = false) List<MultipartFile> images,
        @RequestPart(value = "param") @Valid MissingPostUpdateParam missingPostUpdateParam,
        @LoginAccount Account account
    ) {
        return ApiResponse.ok(
            Map.of(RETURN_KEY, missingPostService.updateMissingPost(account, postId, missingPostUpdateParam, images)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{postId}")
    public void deleteMissingPost(@PathVariable Long postId, @LoginAccount Account account) {
        log.info("실종/보호 게시물 삭제 call for {}", postId);
        missingPostService.deleteMissingPost(postId, account);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/{postId}/bookmark")
    public void createMissingPostBookmark(@PathVariable Long postId, @LoginAccount Account account) {
        log.info("실종/보호 북마크 생성 call for {}", postId);
        missingPostBookmarkService.createMissingPostBookmark(postId, account);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{postId}/bookmark")
    public void deleteMissingPostBookmark(@PathVariable Long postId, @LoginAccount Account account) {
        log.info("실종/보호 북마크 삭제 call for {}", postId);
        missingPostBookmarkService.deleteMissingPostBookmark(postId, account);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{postId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<CommentPageResults> getMissingPostComments(@PathVariable Long postId, Pageable pageable) {
        return ApiResponse.ok(commentService.getMissingPostComments(postId, pageable));
    }
}
