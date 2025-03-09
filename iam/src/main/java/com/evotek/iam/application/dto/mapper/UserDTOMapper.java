package com.evotek.iam.application.dto.mapper;

import org.mapstruct.Mapper;

import com.evotek.iam.application.dto.response.UserDTO;
import com.evotek.iam.domain.User;

@Mapper(componentModel = "spring")
public interface UserDTOMapper extends DTOMapper<UserDTO, User> {}
