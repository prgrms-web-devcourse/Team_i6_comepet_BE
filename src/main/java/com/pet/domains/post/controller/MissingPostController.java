package com.pet.domains.post.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import com.pet.domains.post.dto.response.MissingPostReadResults;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class MissingPostController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
        path = "/missing-posts",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<Map<String, Long>> createMissingPost(@RequestBody MissingPostCreateParam param) {
        return ApiResponse.ok(Map.of("id", 1L));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(
        path = "/missing-posts",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<MissingPostReadResults> getMissingPosts() {
        return ApiResponse.ok(
            MissingPostReadResults.of(List.of(
                MissingPostReadResults.MissingPost.of(
                    1L, "서울특별시", "도봉구", "토이푸들", Status.DETECTION, LocalDateTime.now(),
                    SexType.FEMALE, true, 2,
                    "https://post-phinf.pstatic.net/MjAyMTA0MTJfNTAg/MDAxNjE4MjMwNjg1MTEw"
                        + ".ndUKWrXXt0ei-dWJ00ITFpplT2wbx8fGGYeaLAJOTU8g.bbbkMXZBclmiz4PQzealX8o15rbeKzWlr_MggIOSeekg."
                        + "JPEG/IMG_2381.jpg?type=w1200",
                    List.of(
                        MissingPostReadResults.MissingPost.PostTag.of(1L, "고슴도치"),
                        MissingPostReadResults.MissingPost.PostTag.of(2L, "애완동물"),
                        MissingPostReadResults.MissingPost.PostTag.of(3L, "반려동물"),
                        MissingPostReadResults.MissingPost.PostTag.of(4L, "멋있어"),
                        MissingPostReadResults.MissingPost.PostTag.of(5L, "너무예뻐")
                    )
                ),
                MissingPostReadResults.MissingPost.of(
                    2L, "서울특별시", "강남구", "UNKNOWN", Status.MISSING, LocalDateTime.now(),
                    SexType.MALE, false, 0,
                    "https://post-phinf.pstatic.net/MjAyMTA0MTJfMjkw/MDAxNjE4MjMxNDk4Mjg0."
                        + "XhooNOw8J9DsctWoHOyAw7EpQv1XZ3eQGcJEMpTVIZMg.mnJFYVFpRpn98aXT5bhX3-H-yIYSj7caPM0VZKhcUeEg."
                        + "JPEG/IMG_2370.jpg?type=w1200",
                    List.of(
                        MissingPostReadResults.MissingPost.PostTag.of(1L, "고슴도치"),
                        MissingPostReadResults.MissingPost.PostTag.of(2L, "애완동물"),
                        MissingPostReadResults.MissingPost.PostTag.of(3L, "반려동물")
                    )
                ),
                MissingPostReadResults.MissingPost.of(
                    3L, "서울특별시", "송파구", "비숑", Status.PROTECTION, LocalDateTime.now(),
                    SexType.MALE, false, 5,
                    "https://post-phinf.pstatic.net/MjAyMTA0MTJfMTAw/MDAxNjE4MjMwMjQ0Mjcy"
                        + ".UcHomwacpcXaJ8_nUksje4UkxE7UOzZ0gcgdZTnl0eEg.hh6qgDmsklQHWhuV2cyTqb6T0CyRF_IxNxy4RseU95Ag."
                        + "JPEG/IMG_2379.jpg?type=w1200",
                    List.of(
                        MissingPostReadResults.MissingPost.PostTag.of(1L, "춘식이"),
                        MissingPostReadResults.MissingPost.PostTag.of(2L, "다리밑에서 데려옴"),
                        MissingPostReadResults.MissingPost.PostTag.of(3L, "세젤예 귀요미"),
                        MissingPostReadResults.MissingPost.PostTag.of(4L, "고구마 사줄까?")
                    )
                ),
                MissingPostReadResults.MissingPost.of(
                    4L, "서울특별시", "송파구", "비숑", Status.PROTECTION, LocalDateTime.now(),
                    SexType.MALE, false, 5,
                    "https://post-phinf.pstatic.net/MjAyMTA0MTJfMTAw/MDAxNjE4MjMwMjQ0Mjcy"
                        + ".UcHomwacpcXaJ8_nUksje4UkxE7UOzZ0gcgdZTnl0eEg.hh6qgDmsklQHWhuV2cyTqb6T0CyRF_IxNxy4RseU95Ag."
                        + "JPEG/IMG_2379.jpg?type=w1200",
                    List.of(
                        MissingPostReadResults.MissingPost.PostTag.of(1L, "춘식이"),
                        MissingPostReadResults.MissingPost.PostTag.of(2L, "다리밑에서 데려옴"),
                        MissingPostReadResults.MissingPost.PostTag.of(3L, "세젤예 귀요미"),
                        MissingPostReadResults.MissingPost.PostTag.of(4L, "고구마 사줄까?")
                    )
                ),
                MissingPostReadResults.MissingPost.of(
                    5L, "서울특별시", "송파구", "비숑", Status.PROTECTION, LocalDateTime.now(),
                    SexType.MALE, false, 5,
                    "https://post-phinf.pstatic.net/MjAyMTA0MTJfMTAw/MDAxNjE4MjMwMjQ0Mjcy"
                        + ".UcHomwacpcXaJ8_nUksje4UkxE7UOzZ0gcgdZTnl0eEg.hh6qgDmsklQHWhuV2cyTqb6T0CyRF_IxNxy4RseU95Ag."
                        + "JPEG/IMG_2379.jpg?type=w1200",
                    List.of(
                        MissingPostReadResults.MissingPost.PostTag.of(1L, "춘식이"),
                        MissingPostReadResults.MissingPost.PostTag.of(2L, "다리밑에서 데려옴"),
                        MissingPostReadResults.MissingPost.PostTag.of(3L, "세젤예 귀요미"),
                        MissingPostReadResults.MissingPost.PostTag.of(4L, "고구마 사줄까?")
                    )
                ),
                MissingPostReadResults.MissingPost.of(
                    6L, "서울특별시", "송파구", "비숑", Status.PROTECTION, LocalDateTime.now(),
                    SexType.MALE, false, 5,
                    "https://post-phinf.pstatic.net/MjAyMTA0MTJfMTAw/MDAxNjE4MjMwMjQ0Mjcy"
                        + ".UcHomwacpcXaJ8_nUksje4UkxE7UOzZ0gcgdZTnl0eEg.hh6qgDmsklQHWhuV2cyTqb6T0CyRF_IxNxy4RseU95Ag."
                        + "JPEG/IMG_2379.jpg?type=w1200",
                    List.of(
                        MissingPostReadResults.MissingPost.PostTag.of(1L, "춘식이"),
                        MissingPostReadResults.MissingPost.PostTag.of(2L, "다리밑에서 데려옴"),
                        MissingPostReadResults.MissingPost.PostTag.of(3L, "세젤예 귀요미"),
                        MissingPostReadResults.MissingPost.PostTag.of(4L, "고구마 사줄까?")
                    )
                )
            ),
            10,
            false,
            10
            )
        );
    }

}
