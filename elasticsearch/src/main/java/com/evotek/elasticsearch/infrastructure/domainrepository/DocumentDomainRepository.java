package com.evotek.elasticsearch.infrastructure.domainrepository;

import java.util.List;

public interface DocumentDomainRepository<D, ID> {
    D save(D domainDocument);

    List<D> saveAll(List<D> domainDocuments);

    D getById(ID id);
}
