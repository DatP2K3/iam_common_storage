package com.evotek.iam.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evotek.iam.infrastructure.persistence.entity.RolePermissionEntity;

public interface RolePermissionEntityRepository extends JpaRepository<RolePermissionEntity, UUID> {
    List<RolePermissionEntity> findByRoleIdInAndDeletedFalse(List<UUID> roleIds);
}
