package com.pet.domains.comment.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.common.util.OptimisticLockingHandlingUtils;
import com.pet.domains.account.domain.Account;
import com.pet.domains.comment.domain.Comment;
import com.pet.domains.comment.dto.request.CommentCreateParam;
import com.pet.domains.comment.dto.request.CommentUpdateParam;
import com.pet.domains.comment.dto.response.CommentPageResults;
import com.pet.domains.comment.mapper.CommentMapper;
import com.pet.domains.comment.repository.CommentRepository;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.repository.MissingPostRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final MissingPostRepository missingPostRepository;

    private final CommentMapper commentMapper;

    public CommentPageResults getMissingPostComments(Long postId, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAllByMissingPostId(postId, pageable);
        return commentMapper.toCommentPageResults(commentPage);
    }

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
        Comment foundComment = getMyComment(commentId, account);
        foundComment.updateContent(commentUpdateParam.getContent(), account.getId());
        return foundComment.getId();
    }

    @Transactional
    public void deleteMyCommentById(Account account, Long commentId) {
        Comment foundComment = getMyComment(commentId, account);
        OptimisticLockingHandlingUtils.handling(
            foundComment.getMissingPost()::decreaseCommentCount,
            5,
            "실종 게시글 댓글 카운트 감소"
        );
        commentRepository.delete(foundComment);
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findByIdAndDeletedWithFetch(commentId, false)
            .orElseThrow(ExceptionMessage.NOT_FOUND_COMMENT::getException);

    }

    private Comment getMyComment(Long commentId, Account account) {
        return commentRepository.findByIdAndDeletedWithFetch(commentId, false)
            .filter(comment -> comment.isWriter(account.getId()))
            .orElseThrow(ExceptionMessage.NOT_FOUND_COMMENT::getException);
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

    private MissingPost getMissingPostById(Long postId) {
        return missingPostRepository.findById(postId)
            .orElseThrow(ExceptionMessage.NOT_FOUND_MISSING_POST::getException);
    }

}
