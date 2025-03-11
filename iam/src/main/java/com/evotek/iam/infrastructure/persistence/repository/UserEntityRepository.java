package com.evotek.iam.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.evotek.iam.infrastructure.persistence.entity.UserEntity;
import com.evotek.iam.infrastructure.persistence.repository.custom.UserEntityRepositoryCustom;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, UUID>, UserEntityRepositoryCustom {
    @Query("from UserEntity e where e.locked = false and lower(e.username) = lower(:username)")
    Optional<UserEntity> findByUsername(@Param("username") String username);

    boolean existsByUsername(String username);
}
