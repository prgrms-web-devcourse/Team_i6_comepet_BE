package com.pet.domains.auth.repository;

import com.pet.domains.auth.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

}
