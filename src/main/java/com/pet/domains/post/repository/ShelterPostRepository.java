package com.pet.domains.post.repository;

import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.ShelterPost;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShelterPostRepository extends JpaRepository<ShelterPost, Long> {

    @EntityGraph(attributePaths = {"animalKind", "animalKind.animal", "town", "town.city"}, type = EntityGraphType.LOAD)
    @Override
    Page<ShelterPost> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"animalKind", "animalKind.animal", "town", "town.city"}, type = EntityGraphType.LOAD)
    @Override
    Optional<ShelterPost> findById(Long postId);

    @EntityGraph(attributePaths = {"animalKind", "animalKind.animal", "town", "town.city"}, type = EntityGraphType.LOAD)
    @Query("SELECT sp AS shelterPost, spb.id is not null AS isBookmark FROM ShelterPost sp"
        + " LEFT JOIN ShelterPostBookmark spb ON sp.id = spb.shelterPost AND spb.account=:account")
    Page<ShelterPostWithIsBookmark> findAllWithIsBookmark(Account account, Pageable pageable);

    @EntityGraph(attributePaths = {"animalKind", "animalKind.animal", "town", "town.city"}, type = EntityGraphType.LOAD)
    @Query("SELECT sp AS shelterPost, spb.id is not null AS isBookmark FROM ShelterPost sp"
        + " LEFT JOIN ShelterPostBookmark spb ON sp.id = spb.shelterPost AND spb.account=:account"
        + " WHERE sp.id=:postId")
    ShelterPostWithIsBookmark findByIdWithIsBookmark(Account account, Long postId);
}
