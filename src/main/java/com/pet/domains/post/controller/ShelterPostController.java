package com.pet.domains.post.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.LoginAccount;
import com.pet.domains.post.dto.response.ShelterPostPageResults;
import com.pet.domains.post.dto.response.ShelterPostReadResult;
import com.pet.domains.post.service.ShelterPostBookmarkService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final ShelterPostBookmarkService shelterPostBookmarkService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ShelterPostPageResults> getShelterPosts() {
        return ApiResponse.ok(
            ShelterPostPageResults.of(List.of(
                    ShelterPostPageResults.ShelterPost.of(11L, "서울특별시", "도봉구", 2018L, "https://../2021/11/20211189_s.jpg",
                        "개", "리트리버", LocalDate.of(2021, 11, 25), "경상남도 진주시", true, 10L),
                    ShelterPostPageResults.ShelterPost.of(10L, "서울특별시", "노원구", 2018L, "https://../2021/11/20211189_s.jpg",
                        "개", "믹스견", LocalDate.of(2021, 11, 21), "경상남도 진주시", false, 8L),
                ShelterPostPageResults.ShelterPost.of(9L, "서울특별시", "광진구", 2018L, "https://../2021/11/20211189_s.jpg",
                    "개", "골든 리트리버", LocalDate.of(2021, 11, 19), "경상남도 진주시", true, 8L),
                ShelterPostPageResults.ShelterPost.of(8L, "경기도", "구리시", 2018L, "https://../2021/11/20211189_s.jpg",
                    "개", "삽살개", LocalDate.of(2021, 11, 14), "경상남도 진주시", false, 83L),
                ShelterPostPageResults.ShelterPost.of(7L, "경기도", "구리시", 2014L, "https://../2021/11/20211189_s.jpg",
                    "고양이", "샴", LocalDate.of(2021, 11, 11), "경상남도 진주시", true, 18L),
                ShelterPostPageResults.ShelterPost.of(6L, "경기도", "구리시", 2013L, "https://../2021/11/20211189_s.jpg",
                    "개", "리트리버", LocalDate.of(2021, 11, 3), "경상남도 진주시", false, 82L),
                ShelterPostPageResults.ShelterPost.of(5L, "경기도", "구리시", 2018L, "https://../2021/11/20211189_s.jpg",
                    "고양이", "노르웨이숲", LocalDate.of(2021, 10, 21), "경상남도 진주시", true, 28L),
                ShelterPostPageResults.ShelterPost.of(4L, "경기도", "구리시", 2020L, "https://../2021/11/20211189_s.jpg",
                    "개", "리트리버", LocalDate.of(2021, 10, 21), "경상남도 진주시", true, 38L),
                ShelterPostPageResults.ShelterPost.of(3L, "서울특별시", "강남구", 2013L, "https://../2021/11/20211189_s.jpg",
                    "고양이", "페르시안", LocalDate.of(2021, 10, 14), "경상남도 진주시", true, 84L),
                ShelterPostPageResults.ShelterPost.of(2L, "경기도", "구리시", 2015L, "https://../2021/11/20211189_s.jpg",
                    "개", "리트리버", LocalDate.of(2021, 10, 6), "경상남도 진주시", true, 48L),
                ShelterPostPageResults.ShelterPost.of(1L, "경기도", "구리시", 2017L, "https://../2021/11/20211189_s.jpg",
                    "개", "리트리버", LocalDate.of(2021, 10, 3), "경상남도 진주시", true, 58L)
                ),
                11,
                true,
                10
            ));
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
    public void deleteShelterPostBookmark(@PathVariable Long postId) {
        log.info("북마크 삭제 call for {}", postId);
    }
}
