package com.pet.domains.post.service;

import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.service.AnimalKindService;
import com.pet.domains.post.domain.ShelterPost;
import com.pet.domains.post.dto.request.ShelterPostCreateParams;
import com.pet.domains.post.mapper.ShelterPostMapper;
import com.pet.domains.post.repository.ShelterPostRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ShelterService {

    private final ShelterPostRepository shelterPostRepository;

    private final AnimalKindService animalKindService;

    private final ShelterPostMapper shelterPostMapper;

    @Transactional
    public void bulkCreateShelterPosts(ShelterPostCreateParams shelterPostCreateParams) {
        List<ShelterPost> shelterPosts = new ArrayList<>();
        shelterPostCreateParams.getShelterPosts().forEach(createParam -> shelterPosts.add(
            shelterPostMapper.toEntity(
                createParam,
                getAnimalKind(createParam.getAnimalKindNameFromKindCd())
            )
        ));
        shelterPostRepository.saveAll(shelterPosts);
    }

    private AnimalKind getAnimalKind(String animalKindName) {
        log.info("animalKindName: {}", animalKindName);
        return animalKindService.getOrCreateAnimalKindByEtcAnimal(animalKindName);
    }
}
