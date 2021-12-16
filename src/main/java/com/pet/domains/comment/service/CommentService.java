package com.pet.domains.comment.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.common.util.OptimisticLockingHandlingUtils;
import com.pet.domains.account.domain.Account;
import com.pet.domains.comment.domain.Comment;
import com.pet.domains.comment.dto.request.CommentCreateParam;
import com.pet.domains.comment.dto.request.CommentUpdateParam;
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
        MissingPost missingPost = getMissingPostById(commentCreateParam.getPostId());
        OptimisticLockingHandlingUtils.handling(
            missingPost::increaseCommentCount,
            5,
            "실종 게시글 댓글 카운트 증가"
        );
        return commentRepository.save(getNewComment(account, commentCreateParam, missingPost)).getId();
    }

    @Transactional
    public Long updateComment(Long commentId, CommentUpdateParam commentUpdateParam, Account account) {
        Comment foundComment = getComment(commentId);
        foundComment.updateContent(commentUpdateParam.getContent(), account.getId());

        return foundComment.getId();
    }

    @Transactional
    public void deleteMyCommentById(Account account, Long commentId) {
        Comment foundComment = commentRepository.findById(commentId)
            .filter(comment -> comment.isWriter(account.getId()))
            .orElseThrow(ExceptionMessage.NOT_FOUND_COMMENT::getException);
        OptimisticLockingHandlingUtils.handling(
            foundComment.getMissingPost()::decreaseCommentCount,
            5,
            "실종 게시글 댓글 카운트 감소"
        );
        commentRepository.delete(foundComment);
    }

    private Comment getNewComment(Account account, CommentCreateParam commentCreateParam, MissingPost missingPost) {
        if (Objects.nonNull(commentCreateParam.getParentCommentId())) {
            return Comment.ChildCommentBuilder()
                .content(commentCreateParam.getContent())
                .parentComment(getComment(commentCreateParam.getParentCommentId()))
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

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(ExceptionMessage.NOT_FOUND_COMMENT::getException);
    }

    private MissingPost getMissingPostById(Long postId) {
        return missingPostRepository.findById(postId)
            .orElseThrow(ExceptionMessage.NOT_FOUND_MISSING_POST::getException);
    }

}
