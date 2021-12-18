package com.pet.domains.post.repository;

import com.pet.domains.post.domain.ShelterPost;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShelterPostRepository extends JpaRepository<ShelterPost, Long>, ShelterPostCustomRepository {

    @EntityGraph(attributePaths = {"animalKind", "animalKind.animal", "town", "town.city"}, type = EntityGraphType.LOAD)
    @Query("select sp from ShelterPost sp where sp.id = :postId")
    Optional<ShelterPost> findByIdWithFetch(Long postId);

}

