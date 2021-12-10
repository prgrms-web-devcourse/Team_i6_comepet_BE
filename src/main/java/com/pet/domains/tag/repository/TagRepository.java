package com.pet.domains.tag.repository;

import com.pet.domains.tag.domain.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findTagByName(String name);

    @Query("select t.count from Tag t where t.id = :tagId")
    long findTagCountById(@Param("tagId") Long id);

//    boolean existsTagByName(String name);

}
