package com.evotek.storage.infrastructure.domainrepository;

import java.util.List;
import java.util.Optional;

public interface DomainRepository<D, ID> {
    Optional<D> findById(ID id);
    D save(D entity);
    List<D> saveAll(List<D> entities);
    List<D> findAllByIds(List<ID> ids);
}
