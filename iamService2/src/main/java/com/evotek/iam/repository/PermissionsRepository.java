package com.evotek.iam.repository;

import com.evotek.iam.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionsRepository extends JpaRepository<Permission, Integer>, PermissionCustom {
    Permission findByResourceIdAndScope(String resourceId, String scope);
    @Query("SELECT p " +
            "FROM Role r " +
            "LEFT JOIN RolePermission rp ON r.id = rp.roleId " +
            "LEFT JOIN Permission p ON rp.permissionId = p.id " +
            "WHERE r.id = :roleId")
    List<Permission> findPermissionByRoleId(@Param("roleId") int roleId);
}
