package com.pet.domains.comment.mapper;

import com.pet.domains.account.domain.Account;
import com.pet.domains.comment.domain.Comment;
import com.pet.domains.comment.dto.response.CommentPageResults;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "image", source = "account.image.name")
    CommentPageResults.Comment.Account toAccountResult(Account account);

    List<CommentPageResults.Comment.ChildComment> toChildCommentResults(List<Comment> childComments);

    List<CommentPageResults.Comment> toCommentResult(List<Comment> comments);

    default CommentPageResults toCommentPageResults(Page<Comment> commentPage) {
        return new CommentPageResults(
            toCommentResult(commentPage.getContent()),
            commentPage.getTotalElements(),
            commentPage.isLast(),
            commentPage.getSize()
        );
    }

}
