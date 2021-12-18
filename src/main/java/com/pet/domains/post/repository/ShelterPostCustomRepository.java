package com.pet.domains.post.repository;

import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.ShelterPost;
import com.pet.domains.post.dto.serach.PostSearchParam;
import com.pet.domains.post.repository.projection.ShelterPostWithIsBookmark;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShelterPostCustomRepository {

    Page<ShelterPost> findAllWithFetch(Pageable pageable, PostSearchParam postSearchParam);

    Page<ShelterPostWithIsBookmark> findAllWithIsBookmark(Account account, Pageable pageable,
        PostSearchParam postSearchParam);

    Optional<ShelterPostWithIsBookmark> findByIdWithIsBookmark(Account account, Long postId);

}
