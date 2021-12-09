package com.pet.domains.post.domain;

import com.pet.domains.comment.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<MissingPost, Long> {
}
