package com.pet.domains.tag.service;

import com.pet.domains.tag.domain.Tag;
import com.pet.domains.tag.repository.TagRepository;
import java.util.Optional;
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
        Optional<Tag> tag = tagRepository.findTagByName(tagName);

        if (tag.isPresent()) {
            tag.get().changeCount(tag.get().getId());
            return tag.get();
        }

        return tagRepository.save(
            Tag.builder()
                .name(tagName)
                .build()
        );
    }

}
