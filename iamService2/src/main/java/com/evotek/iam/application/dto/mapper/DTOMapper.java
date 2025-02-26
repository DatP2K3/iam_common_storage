package com.evotek.iam.application.dto.mapper;

import java.util.List;

public interface DTOMapper<D, M, E> {
    D domainModelToDTO(M model);
    M dtoToDomainModel(D dto);
    E domainModelToEntity(M model);
    M entityToDomainModel(E entity);
}
