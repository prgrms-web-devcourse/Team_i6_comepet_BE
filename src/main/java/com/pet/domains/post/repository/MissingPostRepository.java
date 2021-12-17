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

    @EntityGraph(attributePaths = {"animalKind", "animalKind.animal", "town", "town.city"}, type = EntityGraphType.LOAD)
    @Query("select m from MissingPost m")
    Page<MissingPost> findAlWithFetch(Pageable pageable);

    @EntityGraph(attributePaths = {"animalKind", "animalKind.animal", "town", "town.city"}, type = EntityGraphType.LOAD)
    @Query("SELECT DISTINCT mp as missingPost, mpb.id IS NOT NULL as isBookmark FROM MissingPost mp "
        + "LEFT OUTER JOIN MissingPostBookmark mpb ON mp.id = mpb.missingPost.id AND mpb.account = :account "
        + "WHERE mp.deleted = false")
    Page<MissingPostWithIsBookmark> findAllWithIsBookmarkAccountByDeletedIsFalse(Account account, Pageable pageable);

    // todo 쿼리 수정
    @Query("select mp from MissingPost mp "
        + "join AnimalKind ak on mp.account.id =:accountId "
        + "join Town t on mp.town.id = t.id "
        + "join City c on c.id = t.city.id "
        + "join PostTag pt on pt.missingPost.id = mp.id "
        + "join Tag g on pt.tag.id = g.id")
    Page<MissingPost> findByAccountId(Long accountId, Pageable pageable);

}
