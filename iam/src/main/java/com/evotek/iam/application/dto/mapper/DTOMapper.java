package com.evotek.iam.application.dto.mapper;

public interface DTOMapper<D, M> {
    D domainModelToDTO(M model);

    M dtoToDomainModel(D dto);
}
