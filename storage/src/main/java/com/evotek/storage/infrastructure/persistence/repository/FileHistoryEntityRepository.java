package com.evotek.storage.infrastructure.persistence.repository;

import com.evotek.storage.infrastructure.persistence.entity.FileHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileHistoryEntityRepository extends JpaRepository<FileHistoryEntity, UUID> {
}
