package com.evotek.iam.infrastructure.persistence.repository;

import com.evotek.iam.infrastructure.persistence.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PermissionEntityRepository extends JpaRepository<PermissionEntity, UUID> {
    PermissionEntity findByResourceIdAndScope(String resourceId, String scope);
    List<PermissionEntity> findByIdIn(List<UUID> ids);
    @Query("SELECT p " +
            "FROM RoleEntity r " +
            "LEFT JOIN RolePermissionEntity rp ON r.id = rp.roleId " +
            "LEFT JOIN PermissionEntity p ON rp.permissionId = p.id " +
            "WHERE r.id = :roleId")
    List<PermissionEntity> findPermissionByRoleId(@Param("roleId") UUID roleId);
}
