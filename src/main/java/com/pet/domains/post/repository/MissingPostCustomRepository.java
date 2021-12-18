package com.pet.domains.post.repository;

import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.dto.serach.PostSearchParam;
import com.pet.domains.post.repository.projection.MissingPostWithIsBookmarkNew;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MissingPostCustomRepository {

    Page<MissingPost> findMissingPostAllWithFetch(Pageable pageable, PostSearchParam postSearchParam);

    Page<MissingPostWithIsBookmarkNew> findMissingPostAllWithIsBookmark(
        Account account,
        Pageable pageable,
        PostSearchParam postSearchParam
    );

    Optional<MissingPostWithIsBookmarkNew> findMissingPostByIdWithIsBookmark(Account account, Long postId);

}
