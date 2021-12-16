package com.pet.domains.post.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.service.AnimalKindService;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.service.TownService;
import com.pet.domains.post.domain.ShelterPost;
import com.pet.domains.post.dto.request.ShelterPostCreateParams;
import com.pet.domains.post.dto.response.ShelterPostPageResults;
import com.pet.domains.post.dto.response.ShelterPostReadResult;
import com.pet.domains.post.mapper.ShelterPostMapper;
import com.pet.domains.post.repository.ShelterPostRepository;
import com.pet.domains.post.repository.projection.ShelterPostWithIsBookmark;
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

    public ShelterPostPageResults getShelterPostsPageWithAccount(Account account, Pageable pageable) {
        Page<ShelterPostWithIsBookmark> pageResult = shelterPostRepository.findAllWithIsBookmark(account, pageable);
        return shelterPostMapper.toShelterPostPageResultsWithAccount(pageResult);
    }

    public ShelterPostPageResults getShelterPostsPage(Pageable pageable) {
        Page<ShelterPost> pageResult = shelterPostRepository.findAll(pageable);
        return shelterPostMapper.toShelterPostPageResults(pageResult);
    }

    public ShelterPostReadResult getShelterPostReadResultWithAccount(Account account, Long postId) {
        ShelterPostWithIsBookmark foundShelterPost = shelterPostRepository.findByIdWithIsBookmark(account, postId)
            .orElseThrow(ExceptionMessage.NOT_FOUND_SHELTER_POST::getException);
        return shelterPostMapper.toShelterPostReadResult(
            foundShelterPost.getShelterPost(),
            foundShelterPost.isBookmark()
        );
    }

    public ShelterPostReadResult getShelterPostReadResult(Long postId) {
        return shelterPostMapper.toShelterPostReadResult(getShelterPost(postId));
    }

    @Transactional
    public void bulkCreateShelterPost(ShelterPostCreateParams shelterPostCreateParams) {
        shelterPostRepository.saveAll(shelterPostCreateParams.getShelterPosts().stream()
            .map(createParam -> shelterPostMapper.toEntity(
                createParam,
                getAnimalKind(createParam.getAnimalKindNameFromKindCd()),
                getTown(createParam.getCityNameFromAddress(), createParam.getTownNameFromAddress())
            )).collect(Collectors.toList()));
    }

    private ShelterPost getShelterPost(Long postId) {
        return shelterPostRepository.findById(postId)
            .orElseThrow(ExceptionMessage.NOT_FOUND_SHELTER_POST::getException);
    }

    private AnimalKind getAnimalKind(String animalKindName) {
        log.debug("animalKindName: {}", animalKindName);
        return animalKindService.getOrCreateAnimalKindByEtcAnimal(animalKindName);
    }

    private Town getTown(String cityName, String townName) {
        log.debug("cityName: {}, townName: {}", cityName, townName);
        return townService.getOrCreateTownByName(cityName, townName);
    }

}
