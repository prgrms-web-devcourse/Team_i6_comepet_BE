package com.pet.domains.post.repository;

import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.MissingPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MissingPostRepository extends JpaRepository<MissingPost, Long> {

    @EntityGraph(attributePaths = {"animalKind", "animalKind.animal", "town", "town.city", "postTags",
        "postTags.tag"}, type = EntityGraphType.LOAD)
    Page<MissingPost> findAllByDeletedIsFalse(Pageable pageable);

    @EntityGraph(attributePaths = {"animalKind", "animalKind.animal", "town", "town.city", "postTags",
        "postTags.tag"}, type = EntityGraphType.LOAD)
    @Query("SELECT mp as missingPost, mpb.id IS NOT NULL as isBookmark FROM MissingPost mp "
        + "LEFT OUTER JOIN MissingPostBookmark mpb ON mp.id = mpb.missingPost.id AND mpb.account = :account "
        + "WHERE mp.deleted = false")
    Page<MissingPostWithIsBookmark> findAllWithIsBookmarkAccountByDeletedIsFalse(Account account, Pageable pageable);

}
