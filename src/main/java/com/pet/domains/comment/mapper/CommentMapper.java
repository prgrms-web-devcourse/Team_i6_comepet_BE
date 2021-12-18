package com.pet.domains.comment.mapper;

import com.pet.domains.account.domain.Account;
import com.pet.domains.comment.domain.Comment;
import com.pet.domains.comment.dto.response.CommentPageResults;
import com.pet.domains.comment.dto.response.CommentWriteResult;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "image", source = "account.image.name")
    CommentPageResults.Comment.Account toAccountReadResult(Account account);

    List<CommentPageResults.Comment.ChildComment> toChildCommentReadResults(List<Comment> childComments);

    List<CommentPageResults.Comment> toCommentResult(List<Comment> comments);

    default CommentPageResults toCommentPageResults(Page<Comment> commentPage) {
        return new CommentPageResults(
            toCommentResult(commentPage.getContent()),
            commentPage.getTotalElements(),
            commentPage.isLast(),
            commentPage.getSize()
        );
    }

    List<CommentWriteResult.ChildComment> toChildCommentWriteResults(List<Comment> childComments);

    @Mapping(target = "image", source = "account.image.name")
    CommentWriteResult.Account toAccountWriteResult(Account account);

    CommentWriteResult toCommentWriteResult(Comment comment);


}
