package com.pet.domains.post.repository;

import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.MissingPost;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MissingPostRepository extends JpaRepository<MissingPost, Long>, MissingPostCustomRepository {

    @EntityGraph(
        attributePaths = {"animalKind", "animalKind.animal", "town", "town.city", "account"},
        type = EntityGraphType.LOAD
    )
    @Query("SELECT mp FROM MissingPost mp WHERE mp.id = :postId")
    Optional<MissingPost> findByMissingPostId(Long postId);


    @EntityGraph(attributePaths = {"animalKind", "town", "town.city"},
        type = EntityGraphType.LOAD)
    Page<MissingPost> findByAccountId(Long accountId, Pageable pageable);

    void deleteAllByAccount(Account account);

}
