package com.evo.common.repository;

import java.util.List;

public interface DomainRepository<D, ID> {
    D save(D entity);

    List<D> saveAll(List<D> domains);

    List<D> findAllByIds(List<ID> ids);

    D getById(ID id);
}
