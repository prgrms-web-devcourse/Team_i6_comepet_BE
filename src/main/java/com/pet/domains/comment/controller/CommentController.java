package com.pet.domains.comment.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.LoginAccount;
import com.pet.domains.comment.dto.request.CommentCreateParam;
import com.pet.domains.comment.dto.request.CommentUpdateParam;
import com.pet.domains.comment.service.CommentService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<Object> createComment(
        @LoginAccount Account account,
        @RequestBody @Valid CommentCreateParam commentCreateParam
    ) {
        return ApiResponse.ok(commentService.createComment(account, commentCreateParam));
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(
        path = "/{commentId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<Object> updateComment(
        @PathVariable Long commentId,
        @RequestBody @Valid CommentUpdateParam commentUpdateParam,
        @LoginAccount Account account
    ) {
        return ApiResponse.ok(commentService.updateComment(commentId, commentUpdateParam, account));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{commentId}")
    public void deleteComment(@LoginAccount Account account, @PathVariable Long commentId) {
        commentService.deleteMyCommentById(account, commentId);
    }
}
