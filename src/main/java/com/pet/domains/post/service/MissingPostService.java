package com.pet.domains.post.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.common.util.OptimisticLockingHandlingUtils;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.dto.response.AccountBookmarkPostPageResults;
import com.pet.domains.account.service.NotificationAsyncService;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.repository.AnimalKindRepository;
import com.pet.domains.animal.service.AnimalKindService;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.repository.TownRepository;
import com.pet.domains.comment.repository.CommentRepository;
import com.pet.domains.image.domain.Image;
import com.pet.domains.image.domain.PostImage;
import com.pet.domains.image.repository.PostImageRepository;
import com.pet.domains.image.service.ImageService;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import com.pet.domains.post.dto.request.MissingPostUpdateParam;
import com.pet.domains.post.dto.response.MissingPostReadResult;
import com.pet.domains.post.dto.response.MissingPostReadResults;
import com.pet.domains.post.dto.serach.PostSearchParam;
import com.pet.domains.post.mapper.MissingPostMapper;
import com.pet.domains.post.mapper.MissingPostReadResultMapper;
import com.pet.domains.post.repository.MissingPostRepository;
import com.pet.domains.post.repository.projection.MissingPostWithIsBookmark;
import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
import com.pet.domains.tag.repository.PostTagRepository;
import com.pet.domains.tag.repository.TagRepository;
import com.pet.domains.tag.service.TagService;
import java.util.ArrayList;
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

    private final AnimalKindRepository animalKindRepository;

    private final TownRepository townRepository;

    private final TagRepository tagRepository;

    private final PostTagRepository postTagRepository;

    private final PostImageRepository postImageRepository;

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

        List<PostTag> getPostTags = postTagRepository.getPostTagsByMissingPostId(getMissingPost.getId());
        OptimisticLockingHandlingUtils.handling(
            () -> tagService.decreaseTagCount(getPostTags),
            5,
            "게시글 삭제시 태그 카운트 감소"
        );

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
        increaseViewCount(missingPost);
        return missingPostMapper.toMissingPostDto(missingPost);
    }

    @Transactional
    public MissingPostReadResult getMissingPostOneWithAccount(Account account, Long postId) {
        MissingPostWithIsBookmark postWithIsBookmarkNew =
            missingPostRepository.findMissingPostByIdWithIsBookmark(account, postId)
                .orElseThrow(ExceptionMessage.NOT_FOUND_MISSING_POST::getException);

        increaseViewCount(postWithIsBookmarkNew.getMissingPost());

        return missingPostReadResultMapper.toMissingPostReadResult(
            postWithIsBookmarkNew.getMissingPost(),
            postWithIsBookmarkNew.getAnimalKind(),
            postWithIsBookmarkNew.getAnimal(),
            postWithIsBookmarkNew.getTown(),
            postWithIsBookmarkNew.getCity(),
            postWithIsBookmarkNew.isBookmark(),
            postWithIsBookmarkNew.getMissingPost().getPostTags(),
            postWithIsBookmarkNew.getMissingPost().getPostImages()
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

    private void increaseViewCount(MissingPost missingPost) {
        OptimisticLockingHandlingUtils.handling(
            missingPost::increaseViewCount,
            5,
            "실종 게시글 조회수 증감 로직"
        );
    }

    @Transactional
    public Long updateMissingPost(Account account, Long postId, MissingPostUpdateParam param,
        List<MultipartFile> multipartFiles) {
        log.info("start update missing post");
        if (Objects.nonNull(multipartFiles) && Objects.nonNull(param.getImages())
            && (multipartFiles.size() + param.getImages().size()) > 3 || multipartFiles.size() > 3) {
            throw ExceptionMessage.INVALID_IMAGE_COUNT.getException();
        }

        //1. 게시글 조회
        MissingPost getMissingPost = missingPostRepository.findById(postId)
            .filter(post -> post.getAccount().getId().equals(account.getId()))
            .orElseThrow(ExceptionMessage.UN_IDENTIFICATION::getException);

        //2. param으로 가져온 tag들 현재 list와 비교하기
        //tag 이름들 가져오기 - O
        List<String> getParamTags =
            param.getTags()
                .stream()
                .map(MissingPostUpdateParam.Tag::getName)
                .collect(Collectors.toList());

        //기존 태그 리스트 가져오기
        List<String> getEntityTags =
            getMissingPost.getPostTags()
                .stream()
                .map(postTag -> postTag.getTag().getName())
                .collect(Collectors.toList());

        //변경되지 않은 태그
        List<String> getUsedTags = getOrCreateUsedTags(getMissingPost, getParamTags, getEntityTags);

        //tag 개수 -1
        decreaseCountNotUsedTags(getMissingPost, getUsedTags);

        //삭제된 태그 삭제
        //1. 리스트 삭제
        //2. postTag 삭제 - dirty checking
        deleteNotUsedTags(getMissingPost, getEntityTags, getUsedTags);

        //3. param으로 가져온 image들 현재 list와 비교하기
        List<Long> getParamImagesId =
            param.getImages().stream()
                .map(MissingPostUpdateParam.Image::getId)
                .collect(Collectors.toList());

        List<Long> getEntityImagesId =
            getMissingPost.getPostImages().stream()
                .map(PostImage::getId)
                .collect(Collectors.toList());

        //변경되지 않은 이미지
        List<Long> getRemovedImages = getRemovedImages(getMissingPost, getParamImagesId, getEntityImagesId);

        //사용하지 않는 image는 missingPost와 postImage에서 삭제
        getRemovedImages.stream()
            .map(getRemovedImage -> postImageRepository.findById(getRemovedImage)
                .orElseThrow(ExceptionMessage.NOT_FOUND_POST_IMAGE::getException))
            .forEach(getPostImage -> {
                getMissingPost.getPostImages().remove(getPostImage);
                postImageRepository.deleteById(getPostImage.getId());
            });

        //새로 받아온 이미지들 넣어주기
        List<Image> imageFiles = uploadAndGetImages(multipartFiles);
        createPostImage(imageFiles, getMissingPost);

        //썸네일 바뀌는 경우의 수
        List<PostImage> getPostImages = postImageRepository.findAllByMissingPostId(getMissingPost.getId());
        String thumbnail = getUpdateThumbnail(getPostImages);

        //4. param으로 가져온 값들 넣어주면서 update
        Town getTown =
            townRepository.findById(param.getTownId()).orElseThrow(ExceptionMessage.NOT_FOUND_TOWN::getException);
        AnimalKind getAnimalKind = animalKindRepository.findByName(param.getAnimalKindName())
            .orElseThrow(ExceptionMessage.NOT_FOUND_ANIMAL_KIND::getException);
        getMissingPost.changeInfo(param.getStatus(), param.getDate(), getTown, param.getDetailAddress(),
            param.getTelNumber(), getAnimalKind, param.getAge(), param.getSex(), param.getChipNumber(),
            param.getContent(), thumbnail);

        log.info("complete update missing post");
        return getMissingPost.getId();
    }

    private List<Long> getRemovedImages(MissingPost getMissingPost, List<Long> getParamImagesId,
        List<Long> getEntityImagesId) {
        List<Long> getRemovedImages = new ArrayList<>();
        getEntityImagesId.stream()
            .filter(imageId -> !getParamImagesId.contains(imageId))
            .forEach(imageId -> {
                getMissingPost.getPostImages().remove(postImageRepository.findById(imageId)
                    .orElseThrow(ExceptionMessage.NOT_FOUND_POST_IMAGE::getException));
                getRemovedImages.add(imageId);
            });
        return getRemovedImages;
    }

    private void deleteNotUsedTags(MissingPost getMissingPost, List<String> getEntityTags, List<String> getUsedTags) {
        getEntityTags.stream().filter(getEntityTag -> !getUsedTags.contains(getEntityTag))
            .map(getEntityTag -> postTagRepository.findByMissingPostAndTag(getMissingPost,
                tagRepository.findTagByName(getEntityTag)
                    .orElseThrow(ExceptionMessage.NOT_FOUND_TAG::getException)))
            .forEach(postTag -> {
                postTagRepository.deleteById(postTag.getId());
                getMissingPost.getPostTags().remove(postTag);
            });
    }

    private void decreaseCountNotUsedTags(MissingPost getMissingPost, List<String> getUsedTags) {
        List<PostTag> getPostTags = postTagRepository.getPostTagsByMissingPostId(getMissingPost.getId());
        List<PostTag> notUsedPostTags =
            getPostTags.stream()
                .filter(postTag -> !getUsedTags.contains(postTag))
                .collect(Collectors.toList());
        tagService.decreaseTagCount(notUsedPostTags);
    }

    private List<String> getOrCreateUsedTags(MissingPost getMissingPost, List<String> getParamTags,
        List<String> getEntityTags) {
        List<String> getUsedTags = new ArrayList<>();
        getParamTags.forEach(getParamTag -> {
            if (!getEntityTags.contains(getParamTag)) {
                //겹치지 않으면 새로운 태그
                PostTag.builder()
                    .missingPost(getMissingPost)
                    .tag(tagService.getOrCreateByTagName(getParamTag))
                    .build();
            } else {
                //겹치면 사용중인 태그
                getUsedTags.add(getParamTag);
            }
        });
        return getUsedTags;
    }

    private String getThumbnail(List<Image> imageFiles) {
        if (CollectionUtils.isEmpty(imageFiles)) {
            return null;
        }
        return imageFiles.get(0).getName();
    }

    private String getUpdateThumbnail(List<PostImage> imageFiles) {
        if (CollectionUtils.isEmpty(imageFiles)) {
            return null;
        }
        return imageFiles.get(0).getImage().getName();
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
