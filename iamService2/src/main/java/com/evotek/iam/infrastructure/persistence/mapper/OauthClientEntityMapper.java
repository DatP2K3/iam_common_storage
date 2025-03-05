package com.evotek.iam.infrastructure.persistence.mapper;

import com.evotek.iam.domain.OauthClient;
import com.evotek.iam.infrastructure.persistence.entity.OauthClientEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface OauthClientEntityMapper extends EntityMapper<OauthClient, OauthClientEntity> {
}
