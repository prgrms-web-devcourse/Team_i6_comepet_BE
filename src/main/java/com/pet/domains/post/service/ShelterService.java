package com.pet.domains.post.service;

import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.service.AnimalKindService;
import com.pet.domains.post.dto.request.ShelterPostCreateParams;
import com.pet.domains.post.mapper.ShelterPostMapper;
import com.pet.domains.post.repository.ShelterPostRepository;
import java.util.stream.Collectors;
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
        shelterPostRepository.saveAll(shelterPostCreateParams.getShelterPosts().stream()
            .map(shelterPostCreateParam -> shelterPostMapper.toEntity(
                shelterPostCreateParam,
                getAnimalKind(shelterPostCreateParam.getAnimalKindNameFromKindCd())
            )).collect(Collectors.toList()));
    }

    private AnimalKind getAnimalKind(String animalKindName) {
        log.debug("animalKindName: {}", animalKindName);
        return animalKindService.getOrCreateAnimalKindByEtcAnimal(animalKindName);
    }
}
