package com.pet.domains.tag.repository;

import com.pet.domains.tag.domain.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findTagByName(String name);

}
