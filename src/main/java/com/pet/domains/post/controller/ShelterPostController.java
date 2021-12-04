package com.pet.domains.post.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.post.dto.response.ShelterPostPageResults;
import com.pet.domains.post.dto.response.ShelterPostPageResults.ShelterPost;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/shelter-post")
@RestController
public class ShelterPostController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ShelterPostPageResults> getShelterPosts() {
        return ApiResponse.ok(
            ShelterPostPageResults.of(List.of(
                ShelterPost.of(1L, "서울특별시", "도봉구", 2018, "https://../2021/11/20211189_s.jpg",
                    "개", "리트리버", LocalDate.of(2021, 11, 25), "경상남도 진주시", false, 10),
                ShelterPost.of(3L, "서울특별시", "구리시", 2018, "https://../2021/11/20211189_s.jpg",
                    "개", "믹스견", LocalDate.of(2021, 11, 21), "경상남도 진주시", false, 8),
                ShelterPost.of(3L, "서울특별시", "구리시", 2018, "https://../2021/11/20211189_s.jpg",
                    "개", "골든 리트리버", LocalDate.of(2021, 11, 19), "경상남도 진주시", false, 8),
                ShelterPost.of(3L, "경기도", "구리시", 2018, "https://../2021/11/20211189_s.jpg",
                    "개", "삽살개", LocalDate.of(2021, 11, 14), "경상남도 진주시", false, 8),
                ShelterPost.of(3L, "경기도", "구리시", 2018, "https://../2021/11/20211189_s.jpg",
                    "고양이", "샴", LocalDate.of(2021, 11, 11), "경상남도 진주시", false, 8),
                ShelterPost.of(3L, "경기도", "구리시", 2018, "https://../2021/11/20211189_s.jpg",
                    "개", "리트리버", LocalDate.of(2021, 11, 3), "경상남도 진주시", false, 8),
                ShelterPost.of(3L, "경기도", "구리시", 2018, "https://../2021/11/20211189_s.jpg",
                    "고양이", "노르웨이숲", LocalDate.of(2021, 10, 21), "경상남도 진주시", false, 8),
                ShelterPost.of(3L, "경기도", "구리시", 2018, "https://../2021/11/20211189_s.jpg",
                    "개", "리트리버", LocalDate.of(2021, 10, 21), "경상남도 진주시", false, 8),
                ShelterPost.of(3L, "경기도", "구리시", 2018, "https://../2021/11/20211189_s.jpg",
                    "개", "리트리버", LocalDate.of(2021, 10, 14), "경상남도 진주시", false, 8),
                ShelterPost.of(3L, "경기도", "구리시", 2018, "https://../2021/11/20211189_s.jpg",
                    "개", "리트리버", LocalDate.of(2021, 10, 06), "경상남도 진주시", false, 8),
                ShelterPost.of(3L, "경기도", "구리시", 2018, "https://../2021/11/20211189_s.jpg",
                    "개", "리트리버", LocalDate.of(2021, 10, 3), "경상남도 진주시", false, 8)
                ),
                11,
                true,
                10
            ));
    }

}
