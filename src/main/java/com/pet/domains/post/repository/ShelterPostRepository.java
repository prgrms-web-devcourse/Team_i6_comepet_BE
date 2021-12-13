package com.pet.domains.post.repository;

import com.pet.domains.post.domain.ShelterPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShelterPostRepository extends JpaRepository<ShelterPost, Long> {


    @EntityGraph(attributePaths = { "animalKind", "animalKind.animal", "town", "town.city"}, type = EntityGraphType.LOAD)
    @Query("SELECT sp AS shelterPost, spb.id IS NOT NULL AS isBookmark FROM ShelterPost sp LEFT JOIN ShelterPostBookmark spb ON sp.id = spb.id")
    Page<ShelterPostWithIsBookmark> findAllWithIsBookmark(Pageable pageable);


}
