package com.pet.domains.tag.service;

import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
import com.pet.domains.tag.repository.PostTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostTagService {

    private final PostTagRepository postTagRepository;

    public void createPostTag(Tag tag, MissingPost missingPost) {
        postTagRepository.save(PostTag.builder()
            .missingPost(missingPost)
            .tag(tag)
            .build());
    }

}
