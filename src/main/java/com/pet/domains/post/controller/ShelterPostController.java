package com.pet.domains.post.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.LoginAccount;
import com.pet.domains.post.dto.response.ShelterPostPageResults;
import com.pet.domains.post.dto.response.ShelterPostReadResult;
import com.pet.domains.post.service.ShelterPostBookmarkService;
import com.pet.domains.post.service.ShelterPostService;
import java.time.LocalDate;
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
    public ApiResponse<ShelterPostPageResults> getShelterPosts(Pageable pageable) {
        return ApiResponse.ok(shelterPostService.getShelterPostsPage(pageable));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ShelterPostReadResult> getShelterPost(@PathVariable Long postId) {
        return ApiResponse.ok(
            ShelterPostReadResult.of(
                1L, 2018L, "경상남도 진주시 집현면 신당길207번길 22 (집현면, 지역농업개발시설)", "진주시청", "055-749-6134", "흰색",
                "https://../2021/11/20211asd89_s.jpg", LocalDate.of(2021, 10, 23), "진주시 일반성면 창촌리 56", "개", "달마시안", "N",
                LocalDate.of(2021, 10, 1), LocalDate.of(2021, 12, 1), "경남-진주-2021-00624", "055-749-5645", "보호중", "F",
                "순함", 7.2, true, 14L)
        );
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
}
