package com.evotek.iam.application.dto.mapper;

import com.evotek.iam.application.dto.response.UserDTO;
import com.evotek.iam.domain.User;
import com.evotek.iam.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOMapper extends DTOMapper<UserDTO, User, UserEntity> {
}
