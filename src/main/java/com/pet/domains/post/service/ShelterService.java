package com.pet.domains.post.service;

import com.pet.domains.post.repository.ShelterPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ShelterService {

    private final ShelterPostRepository shelterPostRepository;


}
