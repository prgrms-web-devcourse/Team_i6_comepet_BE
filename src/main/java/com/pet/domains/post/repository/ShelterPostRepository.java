package com.pet.domains.post.repository;

import com.pet.domains.post.domain.ShelterPost;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterPostRepository extends JpaRepository<ShelterPost, Long>, ShelterPostCustomRepository {

    @EntityGraph(attributePaths = {"animalKind", "animalKind.animal", "town", "town.city"}, type = EntityGraphType.LOAD)
    @Override
    Page<ShelterPost> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"animalKind", "animalKind.animal", "town", "town.city"}, type = EntityGraphType.LOAD)
    @Override
    Optional<ShelterPost> findById(Long postId);

}
