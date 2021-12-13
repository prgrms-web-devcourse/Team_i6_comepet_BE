package com.pet.domains.post.service;

import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.service.AnimalKindService;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.service.TownService;
import com.pet.domains.post.dto.request.ShelterPostCreateParams;
import com.pet.domains.post.dto.response.ShelterPostPageResults;
import com.pet.domains.post.mapper.ShelterPostMapper;
import com.pet.domains.post.repository.ShelterPostRepository;
import com.pet.domains.post.repository.ShelterPostWithIsBookmark;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ShelterPostService {

    private final ShelterPostRepository shelterPostRepository;

    private final AnimalKindService animalKindService;

    private final TownService townService;

    private final ShelterPostMapper shelterPostMapper;

    public ShelterPostPageResults getShelterPostsPage(Pageable pageable) {
        Page<ShelterPostWithIsBookmark> pageResult =
            shelterPostRepository.findAllWithIsBookmark(pageable);
        return shelterPostMapper.toShelterPostPageResults(pageResult);
    }

    @Transactional
    public void bulkCreateShelterPosts(ShelterPostCreateParams shelterPostCreateParams) {
        shelterPostRepository.saveAll(shelterPostCreateParams.getShelterPosts().stream()
            .map(createParam -> shelterPostMapper.toEntity(
                createParam,
                getAnimalKind(createParam.getAnimalKindNameFromKindCd()),
                getTown(createParam.getCityNameFromAddress(), createParam.getTownNameFromAddress())
            )).collect(Collectors.toList()));
    }

    private AnimalKind getAnimalKind(String animalKindName) {
        log.info("animalKindName: {}", animalKindName);
        return animalKindService.getOrCreateAnimalKindByEtcAnimal(animalKindName);
    }

    private Town getTown(String cityName, String townName) {
        System.out.println(cityName);
        System.out.println(townName);
        log.debug("cityName: {}, townName: {}", cityName, townName);
        return townService.getOrCreateTownByName(cityName, townName);
    }
}
