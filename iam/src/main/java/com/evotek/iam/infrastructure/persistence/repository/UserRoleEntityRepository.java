package com.evotek.iam.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evotek.iam.infrastructure.persistence.entity.UserRoleEntity;

public interface UserRoleEntityRepository extends JpaRepository<UserRoleEntity, UUID> {
    List<UserRoleEntity> findByUserIdIn(List<UUID> userIds);
}
