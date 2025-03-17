package com.evotek.storage.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evotek.storage.infrastructure.persistence.entity.FileHistoryEntity;

public interface FileHistoryEntityRepository extends JpaRepository<FileHistoryEntity, UUID> {}
