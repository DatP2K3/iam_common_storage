package com.evotek.storage.repository;

import com.evotek.storage.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer>, FileRepositoryCustom {
}
