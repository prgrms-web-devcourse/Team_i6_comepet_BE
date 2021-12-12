package com.pet.domains.post.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.ShelterPost;
import com.pet.domains.post.domain.ShelterPostBookmark;
import com.pet.domains.post.repository.ShelterPostBookmarkRepository;
import com.pet.domains.post.repository.ShelterPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ShelterPostBookmarkService {

    private final ShelterPostBookmarkRepository shelterPostBookmarkRepository;

    private final ShelterPostRepository shelterPostRepository;

    @Transactional
    public void createPostBookmark(Long postId, Account account) {
        ShelterPost foundPost = shelterPostRepository.findById(postId)
            .orElseThrow(ExceptionMessage.NOT_FOUND_SHELTER_POST::getException);

        shelterPostBookmarkRepository.save(
            ShelterPostBookmark.builder()
                .shelterPost(foundPost)
                .account(account)
                .build()
        );
    }

}
