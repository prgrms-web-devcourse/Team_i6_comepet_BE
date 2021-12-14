package com.pet.domains.image.repository;

import com.pet.domains.image.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

}
