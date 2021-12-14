package com.pet.domains.post.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.domain.MissingPostBookmark;
import com.pet.domains.post.repository.MissingPostBookmarkRepository;
import com.pet.domains.post.repository.MissingPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MissingPostBookmarkService {

    private final MissingPostRepository missingPostRepository;

    private final MissingPostBookmarkRepository missingPostBookmarkRepository;

    @Transactional
    public void createMissingPostBookmark(Long postId, Account account) {
        MissingPost getMissingPost = missingPostRepository.findById(postId)
            .orElseThrow(ExceptionMessage.NOT_FOUND_MISSING_POST::getException);

        missingPostBookmarkRepository.save(
            MissingPostBookmark.builder()
                .account(account)
                .missingPost(getMissingPost)
                .build()
        );
    }

}
