package com.pet.domains.tag.repository;

import com.pet.domains.tag.domain.PostTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    List<PostTag> getPostTagsByMissingPostId(Long postId);

    void deleteAllByMissingPostId(Long postId);

}
