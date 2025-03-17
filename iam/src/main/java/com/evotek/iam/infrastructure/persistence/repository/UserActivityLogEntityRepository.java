package com.evotek.iam.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evotek.iam.infrastructure.persistence.entity.UserActivityLogEntity;

public interface UserActivityLogEntityRepository extends JpaRepository<UserActivityLogEntity, UUID> {}
