package com.pet.domains.tag.service;

import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
import com.pet.domains.tag.repository.TagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public Tag getOrCreateByTagName(String tagName) {
        return tagRepository.findTagByName(tagName)
            .map(tag -> {
                tag.increaseCount();
                return tag;
            })
            .orElseGet(() -> tagRepository.save(
                Tag.builder()
                    .name(tagName)
                    .count(1)
                    .build())
            );
    }

    @Transactional
    public void decreaseTagCount(List<PostTag> postTags) {
        postTags.stream()
            .map(PostTag::getTag)
            .forEach(Tag::decreaseCount);
    }

}
