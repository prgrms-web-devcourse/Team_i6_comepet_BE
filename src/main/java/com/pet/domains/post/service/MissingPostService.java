package com.pet.domains.post.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.common.util.OptimisticLockingHandlingUtils;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.service.NotificationAsyncService;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.service.AnimalKindService;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.repository.TownRepository;
import com.pet.domains.comment.repository.CommentRepository;
import com.pet.domains.image.domain.Image;
import com.pet.domains.image.domain.PostImage;
import com.pet.domains.image.service.ImageService;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import com.pet.domains.post.dto.response.MissingPostReadResult;
import com.pet.domains.post.dto.response.MissingPostReadResults;
import com.pet.domains.post.mapper.MissingPostMapper;
import com.pet.domains.post.repository.MissingPostRepository;
import com.pet.domains.post.repository.MissingPostWithIsBookmark;
import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
import com.pet.domains.tag.repository.PostTagRepository;
import com.pet.domains.tag.service.TagService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MissingPostService {

    private final AnimalKindService animalKindService;

    private final ImageService imageService;

    private final TagService tagService;

    private final NotificationAsyncService notificationAsyncService;

    private final MissingPostRepository missingPostRepository;

    private final TownRepository townRepository;

    private final PostTagRepository postTagRepository;

    private final MissingPostMapper missingPostMapper;

    private final CommentRepository commentRepository;

    @Transactional
    public Long createMissingPost(MissingPostCreateParam missingPostCreateParam, List<MultipartFile> multipartFiles,
        Account account) {
        log.info("start create missing post");
        if (Objects.nonNull(multipartFiles) && multipartFiles.size() > 3) {
            throw ExceptionMessage.INVALID_IMAGE_COUNT.getException();
        }
        AnimalKind animalKind = animalKindService.getOrCreateAnimalKind(missingPostCreateParam.getAnimalId(),
            missingPostCreateParam.getAnimalKindName());
        Town town = townRepository.getById(missingPostCreateParam.getTownId());

        List<Tag> tags = getTags(missingPostCreateParam);
        List<Image> imageFiles = uploadAndGetImages(multipartFiles);
        String thumbnail = getThumbnail(imageFiles);
        MissingPost newMissingPost =
            missingPostMapper.toEntity(missingPostCreateParam, town, animalKind, thumbnail, account);
        createPostTags(tags, newMissingPost);
        createPostImage(imageFiles, newMissingPost);

        MissingPost savedMissingPost = missingPostRepository.save(newMissingPost);
        notificationAsyncService.createNotifications(savedMissingPost);
        log.info("complete create missing post");

        return savedMissingPost.getId();
    }

    @Transactional
    public void deleteMissingPost(Long postId, Account account) {
        MissingPost getMissingPost = missingPostRepository.findById(postId)
            .filter(post -> post.getAccount().getId().equals(account.getId()))
            .orElseThrow(ExceptionMessage.UN_IDENTIFICATION::getException);
        commentRepository.deleteAllByMissingPostId(getMissingPost.getId());

        List<PostTag> getPostTags = postTagRepository.getPostTagsByMissingPostId(getMissingPost.getId());
        OptimisticLockingHandlingUtils.handling(
            () -> tagService.decreaseTagCount(getPostTags),
            5,
            "게시글 삭제시 태그 카운트 감소"
        );

        missingPostRepository.deleteById(getMissingPost.getId());
    }

    public MissingPostReadResults getMissingPostsPage(Pageable pageable) {
        Page<MissingPost> pageResult = missingPostRepository.findAllWithFetch(pageable);
        return missingPostMapper.toMissingPostsResults(pageResult);
    }

    public MissingPostReadResults getMissingPostsPageWithAccount(Account account, Pageable pageable) {
        Page<MissingPostWithIsBookmark> pageResult =
            missingPostRepository.findAllWithIsBookmarkAccountByDeletedIsFalse(account, pageable);
        return missingPostMapper.toMissingPostsWithBookmarkResults(pageResult);
    }

    @Transactional
    public MissingPostReadResult getMissingPostOne(Long postId) {
        MissingPost missingPost =
            missingPostRepository.findByMissingPostId(postId)
                .orElseThrow(ExceptionMessage.NOT_FOUND_MISSING_POST::getException);
        missingPost.increaseViewCount();
        return missingPostMapper.toMissingPostDto(missingPost);
    }

    @Transactional
    public MissingPostReadResult getMissingPostOneWithAccount(Account account, Long postId) {
        MissingPostWithIsBookmark missingPostWithIsBookmark =
            missingPostRepository.findByIdAndWithIsBookmarkAccount(account, postId);
        OptimisticLockingHandlingUtils.handling(
            () -> missingPostWithIsBookmark.getMissingPost().increaseViewCount(),
            5,
            "실종 게시글 조회수 증감 로직"
        );
        return missingPostMapper.toMissingPostDto(missingPostWithIsBookmark);
    }

    private String getThumbnail(List<Image> imageFiles) {
        if (CollectionUtils.isEmpty(imageFiles)) {
            return null;
        }
        return imageFiles.get(0).getName();
    }

    private void createPostTags(List<Tag> tags, MissingPost newMissingPost) {
        if (!CollectionUtils.isEmpty(tags)) {
            tags.forEach(tag -> PostTag.builder()
                .missingPost(newMissingPost)
                .tag(tag)
                .build());
        }
    }

    private void createPostImage(List<Image> imageFiles, MissingPost mappingMissingPost) {
        if (!CollectionUtils.isEmpty(imageFiles)) {
            imageFiles.forEach(image -> PostImage.builder()
                .missingPost(mappingMissingPost)
                .image(image)
                .build());
        }
    }

    private List<Image> uploadAndGetImages(List<MultipartFile> multipartFiles) {
        if (Objects.isNull(multipartFiles)) {
            return Collections.emptyList();
        }
        return multipartFiles.stream()
            .filter(multipartFile -> !StringUtils.isEmpty(multipartFile.getOriginalFilename()))
            .map(imageService::createImage).collect(Collectors.toList());
    }

    private List<Tag> getTags(MissingPostCreateParam missingPostCreateParam) {
        return Objects.requireNonNull(missingPostCreateParam.getTags())
            .stream()
            .map(MissingPostCreateParam.Tag::getName)
            .distinct()
            .map(tagService::getOrCreateByTagName)
            .collect(Collectors.toList());
    }

}
