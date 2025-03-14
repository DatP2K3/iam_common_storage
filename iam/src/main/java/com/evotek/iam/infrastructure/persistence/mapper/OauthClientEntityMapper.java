package com.evotek.iam.infrastructure.persistence.mapper;

import com.evo.common.mapper.EntityMapper;
import org.mapstruct.Mapper;

import com.evotek.iam.domain.OauthClient;
import com.evotek.iam.infrastructure.persistence.entity.OauthClientEntity;

@Mapper(componentModel = "Spring")
public interface OauthClientEntityMapper extends EntityMapper<OauthClient, OauthClientEntity> {}
