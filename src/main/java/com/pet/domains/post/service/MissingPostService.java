package com.pet.domains.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MissingPostService {

//    private final MissingPostRepository missingPostRepository;
//
//    private final AnimalKindService animalKindService;
//
//    private final TownRepository townRepository;
//
//    private final PostImageRepository postImageRepository;
//
//    private final PostTagService postTagService;
//
//    private final TagService tagService;
//
//    @Transactional
//    public Long createMissingPost(MissingPostCreateParam missingPostCreateParam, List<Image> imageFiles) {
//        AnimalKind animalKind = animalKindService.getOrCreateByAnimalKind(missingPostCreateParam.getAnimalId(),
//            missingPostCreateParam.getAnimalKindName());
//        Town town = townRepository.getById(missingPostCreateParam.getTownId());
//
//        List<Tag> tags = new ArrayList<>();
//        if (!CollectionUtils.isEmpty(missingPostCreateParam.getTags())) {
//            tags =
//                missingPostCreateParam.getTags()
//                    .stream()
//                    .map(tag -> tagService.getOrCreateByTagName(tag.getName()))
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//        }
//
//        String thumbnail = null;
//        if (!imageFiles.isEmpty()) {
//            thumbnail = imageFiles.get(0).getName();
//        }
//
//        MissingPost createMissingPost = missingPostRepository.save(
//            MissingPostMapper.INSTANCE.toEntity(missingPostCreateParam, town, animalKind, thumbnail));
//
//        if (!CollectionUtils.isEmpty(tags)) {
//            for (Tag tag : tags) {
//                postTagService.createPostTag(tag, createMissingPost);
//            }
//        }
//
//        if (!CollectionUtils.isEmpty(imageFiles)) {
//            imageFiles.stream().map(image -> PostImage.builder()
//                .missingPost(createMissingPost)
//                .image(image)
//                .build()
//            ).forEach(postImageRepository::save);
//        }
//
//        return createMissingPost.getId();
//    }

}
