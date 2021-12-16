package com.pet.domains.post.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.common.util.OptimisticLockingHandlingUtils;
import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.domain.MissingPostBookmark;
import com.pet.domains.post.repository.MissingPostBookmarkRepository;
import com.pet.domains.post.repository.MissingPostRepository;
import java.util.Objects;
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
        OptimisticLockingHandlingUtils.handling(
            getMissingPost::increaseBookCount,
            5,
            "실종/보호 게시글 북마크 증가"
        );
    }

    @Transactional
    public void deleteMissingPostBookmark(Long postId, Account account) {
        MissingPost getMissingPost = missingPostRepository.findById(postId)
            .orElseThrow(ExceptionMessage.NOT_FOUND_MISSING_POST::getException);

        Long resultCount = missingPostBookmarkRepository.deleteByAccountAndMissingPost(account, getMissingPost);
        if (Objects.nonNull(resultCount) && resultCount == 1L) {
            MissingPost foundPost = missingPostRepository.findById(postId)
                .orElseThrow(ExceptionMessage.NOT_FOUND_MISSING_POST::getException);
            OptimisticLockingHandlingUtils.handling(
                foundPost::decreaseBookCount,
                5,
                "실종/보호 게시글 북마크 감소"
            );
        }
        getMissingPost.decreaseBookCount();
    }

}
