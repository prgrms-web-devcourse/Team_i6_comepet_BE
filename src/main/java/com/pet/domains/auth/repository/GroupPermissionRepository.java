package com.pet.domains.auth.repository;

import com.pet.domains.auth.domain.GroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupPermissionRepository extends JpaRepository<GroupPermission, Long> {

}
