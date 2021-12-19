package com.pet.domains.post.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.dto.response.AccountBookmarkPostPageResults;
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
import com.pet.domains.post.dto.serach.PostSearchParam;
import com.pet.domains.post.mapper.MissingPostMapper;
import com.pet.domains.post.mapper.MissingPostReadResultMapper;
import com.pet.domains.post.repository.MissingPostRepository;
import com.pet.domains.post.repository.projection.MissingPostWithIsBookmark;
import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
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

    private final MissingPostMapper missingPostMapper;

    private final CommentRepository commentRepository;

    private final MissingPostReadResultMapper missingPostReadResultMapper;

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
        missingPostRepository.deleteById(getMissingPost.getId());
    }

    public MissingPostReadResults getMissingPostsPage(Pageable pageable, PostSearchParam param) {
        Page<MissingPost> pageResult = missingPostRepository.findMissingPostAllWithFetch(pageable, param);
        return missingPostReadResultMapper.toMissingPostPageResults(pageResult);
    }

    public MissingPostReadResults getMissingPostsPageWithAccount(Account account, Pageable pageable,
        PostSearchParam searchParam) {
        Page<MissingPostWithIsBookmark> pageResult =
            missingPostRepository.findMissingPostAllWithIsBookmark(account, pageable, searchParam);
        return missingPostReadResultMapper.toMissingPostPageResultsWithAccount(pageResult);
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
            missingPostRepository.findMissingPostByIdWithIsBookmark(account, postId)
                .orElseThrow(ExceptionMessage.NOT_FOUND_MISSING_POST::getException);
        MissingPost missingPost = missingPostWithIsBookmark.getMissingPost();
        missingPost.increaseViewCount();

        return missingPostReadResultMapper.toMissingPostReadResult(
            missingPost,
            missingPostWithIsBookmark.getAnimalKind(),
            missingPostWithIsBookmark.getAnimal(),
            missingPostWithIsBookmark.getTown(),
            missingPostWithIsBookmark.getCity(),
            missingPostWithIsBookmark.isBookmark(),
            missingPost.getPostTags(),
            missingPost.getPostImages()
        );
    }

    public AccountBookmarkPostPageResults getBookmarksThumbnailsByAccount(Account account, Pageable pageable) {
        Page<MissingPostWithIsBookmark> missingPostWithIsBookmarks =
            missingPostRepository.findMissingPostAllWithIsBookmark(account, pageable);
        return AccountBookmarkPostPageResults
            .of(missingPostWithIsBookmarks.stream()
                    .map(missingPostWithIsBookmark -> missingPostMapper
                        .toAccountBookmarkMissingPost(missingPostWithIsBookmark.getMissingPost()))
                    .collect(Collectors.toList()),
                missingPostWithIsBookmarks.getTotalElements(),
                missingPostWithIsBookmarks.isLast(),
                missingPostWithIsBookmarks.getSize());
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
