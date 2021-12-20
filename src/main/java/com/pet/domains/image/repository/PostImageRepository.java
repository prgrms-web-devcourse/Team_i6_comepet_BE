package com.pet.domains.image.repository;

import com.pet.domains.image.domain.PostImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    void deleteAllByMissingPostId(Long postId);

    List<PostImage> findAllByMissingPostId(Long postId);

}
