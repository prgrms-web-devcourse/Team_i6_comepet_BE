package com.pet.domains.tag.service;

import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
import com.pet.domains.tag.repository.PostTagRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostTagService {

    private final PostTagRepository postTagRepository;

    @Transactional
    public void createPostTag(List<Tag> tags, MissingPost missingPost) {
        postTagRepository.saveAll(tags.stream()
            .map(getTag -> PostTag.builder()
                .missingPost(missingPost)
                .tag(getTag)
                .build())
            .collect(Collectors.toList()));
    }

}
