package com.pet.domains.post.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.LoginAccount;
import com.pet.domains.comment.dto.response.CommentPageResults;
import com.pet.domains.comment.service.CommentService;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import com.pet.domains.post.dto.request.MissingPostUpdateParam;
import com.pet.domains.post.dto.response.MissingPostReadResult;
import com.pet.domains.post.dto.response.MissingPostReadResults;
import com.pet.domains.post.service.MissingPostBookmarkService;
import com.pet.domains.post.service.MissingPostService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        // TODO: 2021/12/10 게시물 등록 안에서 알림전송까지 해야한다.
        return ApiResponse.ok(
            Map.of(RETURN_KEY, missingPostService.createMissingPost(missingPostCreateParam, images, account)));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<MissingPostReadResults> getMissingPosts(@LoginAccount Account account, Pageable pageable) {
        log.info("실종/보호 게시물 리스트 조회");
        if (Objects.nonNull(account)) {
            return ApiResponse.ok(missingPostService.getMissingPostsPageWithAccount(account, pageable));
        }
        return ApiResponse.ok(missingPostService.getMissingPostsPage(pageable));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<MissingPostReadResult> getMissingPost(@LoginAccount Account account, @PathVariable Long postId) {
        log.info("실종/보호 게시물 단건 조회");
        if (Objects.nonNull(account)) {
            return ApiResponse.ok(missingPostService.getMissingPostOneWithAccount(account, postId));
        }
        return ApiResponse.ok(missingPostService.getMissingPostOne(postId));
//        return ApiResponse.ok(
//            MissingPostReadResult.of(1L,
//                MissingPostReadResult.User.of(1L, "짱구",
//                    "https://img.insight.co.kr/static/2021/01/10/700/img_20210110130830_kue82l80.webp"
//                ),
//                Status.DETECTION, LocalDate.now(), "경기도", "구리시", "주민센터 앞 골목 근처",
//                "01032430012", "개", "리트리버", 10, SexType.MALE,
//                "410123456789112",
//                List.of(
//                    MissingPostReadResult.Image.of(1L, "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png"),
//                    MissingPostReadResult.Image.of(2L, "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png")
//                ),
//                List.of(
//                    MissingPostReadResult.PostTag.of(1L, "해시태그"),
//                    MissingPostReadResult.PostTag.of(2L, "춘식이")
//                ),
//                "찾아주시면 반드시 사례하겠습니다. 연락주세요", 3, 1, true, 1,
//                List.of(
//                    MissingPostReadResult.Comment.of(1L,
//                        MissingPostReadResult.Comment.User.of(2L, "맹구",
//                            "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png"),
//                        "얼른 찾으시길 바래요 ㅠ", LocalDateTime.now())
//                ), LocalDateTime.now()
//            )
//        );
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(
        path = "/{postId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<Map<String, Long>> updateMissingPost(
        @PathVariable Long postId, @RequestBody MissingPostUpdateParam missingPostUpdateParam
    ) {
        List<MultipartFile> images = missingPostUpdateParam.getFiles();
        StringJoiner stringJoiner = new StringJoiner(",", "[", "]");
        images.stream().map(MultipartFile::getName).forEach(stringJoiner::add);

        log.info("post image size: {}, names: {} ", images.size(), stringJoiner);
        return ApiResponse.ok(Map.of(RETURN_KEY, 1L));
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
