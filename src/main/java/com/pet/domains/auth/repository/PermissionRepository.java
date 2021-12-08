package com.pet.domains.auth.repository;

import com.pet.domains.auth.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
