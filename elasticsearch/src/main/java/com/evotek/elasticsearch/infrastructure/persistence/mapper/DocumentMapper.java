package com.evotek.elasticsearch.infrastructure.persistence.mapper;

import java.util.List;

public interface DocumentMapper<D, E> {
    D toDomainModel(E entity);

    E toDocument(D domain);

    List<D> toDomainModelList(Iterable<E> documentList);

    List<E> toDocumentList(Iterable<D> domainList);
}
