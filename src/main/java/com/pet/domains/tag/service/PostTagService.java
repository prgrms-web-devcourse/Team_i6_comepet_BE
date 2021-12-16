package com.pet.domains.tag.service;

import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostTagService {

    public void createPostTag(List<Tag> tags, MissingPost missingPost) {
        for (Tag tag : tags) {
            PostTag.builder()
                .missingPost(missingPost)
                .tag(tag)
                .build();
        }
    }

}
