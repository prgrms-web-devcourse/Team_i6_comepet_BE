package com.pet.domains.comment.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.comment.domain.Comment;
import com.pet.domains.comment.dto.request.CommentCreateParam;
import com.pet.domains.comment.repository.CommentRepository;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.repository.MissingPostRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final MissingPostRepository missingPostRepository;

    @Transactional
    public Long createComment(Account account, CommentCreateParam commentCreateParam) {
        return commentRepository.save(getNewComment(account, commentCreateParam)).getId();
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(ExceptionMessage.NOT_FOUND_COMMENT::getException);
    }

    private Comment getNewComment(Account account, CommentCreateParam commentCreateParam) {
        MissingPost missingPost = getMissingPostById(commentCreateParam.getPostId());
        if (Objects.nonNull(commentCreateParam.getParentCommentId())) {
            return Comment.ChildCommentBuilder()
                .content(commentCreateParam.getContent())
                .parentComment(getCommentById(commentCreateParam.getParentCommentId()))
                .missingPost(missingPost)
                .account(account)
                .build();
        }
        return Comment.builder()
            .content(commentCreateParam.getContent())
            .missingPost(missingPost)
            .account(account)
            .build();
    }

    private MissingPost getMissingPostById(Long postId) {
        return missingPostRepository.findById(postId)
            .orElseThrow(ExceptionMessage.NOT_FOUND_MISSING_POST::getException);
    }

}
