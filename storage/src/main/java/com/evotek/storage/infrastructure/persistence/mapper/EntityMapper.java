package com.evotek.storage.infrastructure.persistence.mapper;

import java.util.List;

public interface EntityMapper<D, E> {
    D toDomainModel(E entity);

    E toEntity(D domain);

    List<D> toDomainModelList(List<E> entityList);

    List<E> toEntityList(List<D> domainList);
}
