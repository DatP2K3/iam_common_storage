package com.evotek.iam.infrastructure.domainrepository;

import java.util.List;
// ném lên common

public interface DomainRepository<D, ID> {
    D save(D entity);

    List<D> saveAll(List<D> entities);

    List<D> findAllByIds(List<ID> ids);

    D getById(ID id);
}
