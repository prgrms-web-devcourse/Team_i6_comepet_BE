package com.pet.domains.post.service;

import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.repository.AnimalKindRepository;
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
import java.text.MessageFormat;
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

    private final AnimalKindRepository animalKindRepository;

    private final TownRepository townRepository;

    private final PostImageRepository postImageRepository;

    private final PostTagService postTagService;

    private final TagService tagService;

    @Transactional
    public Long createMissingPost(MissingPostCreateParam missingPostCreateParam, List<Image> imageFiles) {
        // TODO: 2021/12/10 품종은 String으로 받아올 것임 : 케빈이 만든 메서드 활용
        AnimalKind animalKind = animalKindRepository.findById(missingPostCreateParam.getAnimalKindId())
            .orElseThrow(() -> new RuntimeException(
                MessageFormat.format("해당 {0}키의 품종을 찾을 수 없습니다.", missingPostCreateParam.getAnimalKindId())));
        Town town = townRepository.findById(missingPostCreateParam.getTownId())
            .orElseThrow(() -> new RuntimeException(
                MessageFormat.format("해당 {0}키의 시군구를 찾을 수 없습니다.", missingPostCreateParam.getTownId())));

        //3. 태그 등록
        //있으면 카운트 + 1
        //없으면 새로 넣어주기
        List<Tag> tags = new ArrayList<>();
        if (!CollectionUtils.isEmpty(missingPostCreateParam.getTags())) {
            tags =
                missingPostCreateParam.getTags()
                    .stream()
                    .map(tag -> tagService.getOrCreateByAnimalKind(tag.getName()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        //썸네일
        String thumbnail = null;
        if (!imageFiles.isEmpty()) {
            thumbnail = imageFiles.get(0).getName();
        }

        //4. 게시물 등록
        MissingPost createMissingPost = missingPostRepository.save(
            MissingPostMapper.INSTANCE.toEntity(missingPostCreateParam, town, animalKind, thumbnail));

        //5. 포스트 태그 등록
        //tags 활용해서 postTag 등록
        if (!CollectionUtils.isEmpty(tags)) {
            for (Tag tag : tags) {
                postTagService.createPostTag(tag, createMissingPost);
            }
        }

        //6. PostImage 등록
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
