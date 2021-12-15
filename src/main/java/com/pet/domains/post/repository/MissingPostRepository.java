package com.pet.domains.post.repository;

import com.pet.domains.post.domain.MissingPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissingPostRepository extends JpaRepository<MissingPost, Long> {

    @EntityGraph(attributePaths = {"animalKind", "animalKind.animal", "town", "town.city", "postTags",
        "postTags.tag"}, type = EntityGraphType.LOAD)
    Page<MissingPost> findAllByDeletedIsFalse(Pageable pageable);

}
