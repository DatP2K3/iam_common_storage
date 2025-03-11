package com.evotek.storage.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evotek.storage.infrastructure.persistence.entity.FileEntity;
import com.evotek.storage.infrastructure.persistence.repository.custom.FileEntityRepositoryCustom;

public interface FileEntityRepository extends JpaRepository<FileEntity, UUID>, FileEntityRepositoryCustom {}
