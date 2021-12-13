package com.pet.domains.post.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.service.AnimalKindService;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.repository.TownRepository;
import com.pet.domains.comment.CommentRepository;
import com.pet.domains.image.domain.Image;
import com.pet.domains.image.domain.PostImage;
import com.pet.domains.image.repository.PostImageRepository;
import com.pet.domains.image.service.ImageService;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import com.pet.domains.post.mapper.MissingPostMapper;
import com.pet.domains.post.repository.MissingPostRepository;
import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
import com.pet.domains.tag.service.PostTagService;
import com.pet.domains.tag.service.TagService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MissingPostService {

    private final MissingPostRepository missingPostRepository;

    private final AnimalKindService animalKindService;

    private final TownRepository townRepository;

    private final PostImageRepository postImageRepository;

    private final PostTagService postTagService;

    private final TagService tagService;

    private final MissingPostMapper missingPostMapper;

    private final ImageService imageService;

    private final CommentRepository commentRepository;

    @Transactional
    public Long createMissingPost(MissingPostCreateParam missingPostCreateParam, List<MultipartFile> multipartFiles,
        Account account) {
        AnimalKind animalKind = animalKindService.getOrCreateAnimalKind(missingPostCreateParam.getAnimalId(),
            missingPostCreateParam.getAnimalKindName());
        Town town = townRepository.getById(missingPostCreateParam.getTownId());

        List<Tag> tags = getTags(missingPostCreateParam);

        List<Image> imageFiles = uploadAndGetImages(multipartFiles);

        String thumbnail = getThumbnail(imageFiles);

        MissingPost createMissingPost = missingPostRepository.save(
            missingPostMapper.toEntity(missingPostCreateParam, town, animalKind, thumbnail, account));

        if (!CollectionUtils.isEmpty(tags) && tags.size() > 0) {
            postTagService.createPostTag(tags, createMissingPost);
        }

        createPostImage(imageFiles, createMissingPost);

        return createMissingPost.getId();
    }

    @Transactional
    public void deleteMissingPost(Long postId, Account account) {
        Optional<MissingPost> getMissingPost = Optional.of(missingPostRepository.getById(postId));
        if (!Objects.equals(getMissingPost.get().getAccount().getId(), account.getId())) {
            throw ExceptionMessage.INVALID_MISSING_POST_WRITER.getException();
        }

        postImageRepository.deleteAllByMissingPostId(postId);

        commentRepository.deleteAllByMissingPostId(postId);

        List<PostTag> postTags = postTagService.getPostTagsByMissingPost(postId);

        if (!CollectionUtils.isEmpty(postTags) && postTags.size() > 0) {
            tagService.decreaseTagCount(postTags);
            postTagService.deletePostTagByMissingPost(postId);
        }

        missingPostRepository.deleteMissingPostByIdAndAccount(postId, account);
    }

    private String getThumbnail(List<Image> imageFiles) {
        String thumbnail = null;
        if (!CollectionUtils.isEmpty(imageFiles) && imageFiles.size() > 0) {
            thumbnail = imageFiles.get(0).getName();
        }
        return thumbnail;
    }

    private void createPostImage(List<Image> imageFiles, MissingPost createMissingPost) {
        if (!CollectionUtils.isEmpty(imageFiles) && imageFiles.size() > 0) {
            imageFiles.stream().map(image -> PostImage.builder()
                .missingPost(createMissingPost)
                .image(image)
                .build()
            ).forEach(postImageRepository::save);
        }
    }

    private List<Image> uploadAndGetImages(List<MultipartFile> multipartFiles) {
        List<Image> imageFiles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(multipartFiles) && multipartFiles.size() > 0) {
            imageFiles = multipartFiles.stream()
                .map(imageService::createImage)
                .collect(Collectors.toList());
        }
        return imageFiles;
    }

    private List<Tag> getTags(MissingPostCreateParam missingPostCreateParam) {
        List<Tag> tags = new ArrayList<>();
        if (!CollectionUtils.isEmpty(missingPostCreateParam.getTags()) && missingPostCreateParam.getTags().size() > 0) {
            tags =
                missingPostCreateParam.getTags()
                    .stream()
                    .map(tag -> tagService.getOrCreateByTagName(tag.getName()))
                    .collect(Collectors.toList());
        }
        return tags;
    }

}
