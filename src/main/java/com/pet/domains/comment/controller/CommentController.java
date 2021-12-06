package com.pet.domains.comment.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.comment.dto.request.CommentCreateParam;
import com.pet.domains.comment.dto.request.CommentUpdateParam;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/comments")
@RestController
public class CommentController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<Object> createComment(@RequestBody CommentCreateParam commentCreateParam) {
        return ApiResponse.ok(Map.of("id", 1L));
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(
        path = "/{postId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<Object> updateComment(
        @PathVariable Long postId,
        @RequestBody CommentUpdateParam commentUpdateParam
    ) {
        return ApiResponse.ok(Map.of("id", 1L));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{postId}")
    public void deleteComment(@PathVariable Long postId) {
        log.info("comment delete call for {}", postId);
    }
}
