package com.pet.domains.post.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.post.dto.response.MissingPostCommentPageResults;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/missing-posts")
@RestController
public class MissingPostsController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{postId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<MissingPostCommentPageResults> getMissingPostComments(@PathVariable Long postId) {
        return ApiResponse.ok(MissingPostCommentPageResults.of(LongStream.rangeClosed(1, 10)
            .mapToObj(iter -> MissingPostCommentPageResults.Comment.of(
                iter,
                "꼭 찾길 바래요. #" + iter,
                LocalDateTime.now(),
                MissingPostCommentPageResults.Comment.User.of(
                    iter, "고양이집사#" + iter, "https://../2021/11/20211189_s.jpg")
                )
            ).collect(Collectors.toList()), 24, false, 10
        ));
    }
}
