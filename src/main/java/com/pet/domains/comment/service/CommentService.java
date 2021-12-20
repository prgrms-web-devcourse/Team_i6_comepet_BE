package com.pet.domains.comment.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.comment.domain.Comment;
import com.pet.domains.comment.dto.request.CommentCreateParam;
import com.pet.domains.comment.dto.request.CommentUpdateParam;
import com.pet.domains.comment.dto.response.CommentPageResults;
import com.pet.domains.comment.dto.response.CommentWriteResult;
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
    public CommentWriteResult createComment(Account account, CommentCreateParam commentCreateParam) {
        MissingPost missingPost = getMissingPostById(commentCreateParam.getPostId());
        return commentMapper.toCommentWriteResult(
            commentRepository.save(getNewComment(account, commentCreateParam, missingPost))
        );
    }

    @Transactional
    public CommentWriteResult updateComment(Long commentId, CommentUpdateParam commentUpdateParam, Account account) {
        Comment foundComment = getMyComment(commentId, account);
        foundComment.updateContent(commentUpdateParam.getContent(), account.getId());

        return commentMapper.toCommentWriteResult(foundComment);
    }

    @Transactional
    public void deleteMyCommentById(Account account, Long commentId) {
        Comment foundComment = getMyComment(commentId, account);
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
