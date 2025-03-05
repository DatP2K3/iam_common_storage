package com.evotek.iam.infrastructure.persistence.mapper;

import com.evotek.iam.domain.Role;
import com.evotek.iam.infrastructure.persistence.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface RoleEntityMapper extends EntityMapper<Role, RoleEntity> {
    void updateEntity(Role domainModel, @MappingTarget RoleEntity roleEntity);
}
