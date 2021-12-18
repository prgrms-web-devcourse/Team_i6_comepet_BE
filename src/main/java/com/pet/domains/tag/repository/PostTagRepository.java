package com.pet.domains.tag.repository;

import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    List<PostTag> getPostTagsByMissingPostId(Long postId);

    void deleteByMissingPost(MissingPost missingPost);

    PostTag findByMissingPostAndTag(MissingPost missingPost, Tag tag);

}
