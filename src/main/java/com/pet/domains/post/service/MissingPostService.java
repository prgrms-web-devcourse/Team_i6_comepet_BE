package com.pet.domains.post.service;

import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.service.AnimalKindService;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.repository.TownRepository;
import com.pet.domains.image.domain.Image;
import com.pet.domains.image.domain.PostImage;
import com.pet.domains.image.repository.PostImageRepository;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import com.pet.domains.post.mapper.MissingPostMapper;
import com.pet.domains.post.repository.MissingPostRepository;
import com.pet.domains.tag.domain.Tag;
import com.pet.domains.tag.service.PostTagService;
import com.pet.domains.tag.service.TagService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

    @Transactional
    public Long createMissingPost(MissingPostCreateParam missingPostCreateParam, List<Image> imageFiles) {
        AnimalKind animalKind = animalKindService.getOrCreateAnimalKind(missingPostCreateParam.getAnimalId(),
            missingPostCreateParam.getAnimalKindName());
        Town town = townRepository.getById(missingPostCreateParam.getTownId());

        List<Tag> tags = new ArrayList<>();
        if (!CollectionUtils.isEmpty(missingPostCreateParam.getTags())) {
            tags =
                missingPostCreateParam.getTags()
                    .stream()
                    .map(tag -> tagService.getOrCreateByTagName(tag.getName()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        String thumbnail = null;
        if (!imageFiles.isEmpty()) {
            thumbnail = imageFiles.get(0).getName();
        }

        MissingPost createMissingPost = missingPostRepository.save(
            missingPostMapper.toEntity(missingPostCreateParam, town, animalKind, thumbnail));

        if (!CollectionUtils.isEmpty(tags)) {
            for (Tag tag : tags) {
                postTagService.createPostTag(tag, createMissingPost);
            }
        }

        if (!CollectionUtils.isEmpty(imageFiles)) {
            imageFiles.stream().map(image -> PostImage.builder()
                .missingPost(createMissingPost)
                .image(image)
                .build()
            ).forEach(postImageRepository::save);
        }

        return createMissingPost.getId();
    }

}
