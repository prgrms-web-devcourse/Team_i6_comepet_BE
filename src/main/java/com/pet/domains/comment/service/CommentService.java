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
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final EntityManager entityManager;

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

    private Comment getMyComment(Long commentId, Account account) {
        Session session = getSession();
        Filter filter = getCommentDeletedFilter(session);
        filter.setParameter(Comment.COMMENT_DELETED_PARAM, false);
        Comment foundComment = commentRepository.findByIdWithFetch(commentId)
            .filter(comment -> comment.isWriter(account.getId()))
            .orElseThrow(ExceptionMessage.NOT_FOUND_COMMENT::getException);
        disableFilter(session);
        return foundComment;
    }

    private Comment getComment(Long commentId) {
        Session session = getSession();
        Filter filter = getCommentDeletedFilter(session);
        filter.setParameter(Comment.COMMENT_DELETED_PARAM, false);
        Comment foundComment = commentRepository.findByIdWithFetch(commentId)
            .orElseThrow(ExceptionMessage.NOT_FOUND_COMMENT::getException);
        disableFilter(session);

        return foundComment;
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Filter getCommentDeletedFilter(Session session) {
        return session.enableFilter(Comment.COMMENT_DELETED_FILTER);
    }

    private void disableFilter(Session session) {
        session.disableFilter(Comment.COMMENT_DELETED_FILTER);
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
