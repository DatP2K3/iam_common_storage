package com.evotek.elasticsearch.infrastructure.domainrepository;

import com.evotek.elasticsearch.infrastructure.persistence.mapper.DocumentMapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class AbstractDocumentDomainRepository<D, E, ID> implements DocumentDomainRepository<D, ID> {
    protected final ElasticsearchRepository<E, ID> repository;
    protected final DocumentMapper<D, E> documentMapper;

    protected AbstractDocumentDomainRepository(ElasticsearchRepository<E, ID> repository, DocumentMapper<D, E> documentMapper) {
        this.repository = repository;
        this.documentMapper = documentMapper;
    }

    @Override
    @Transactional
    public D save(D domainModel) {
        List<D> domainModels = this.saveAll(List.of(domainModel));
        return domainModels.getFirst();
    }

    @Override
    @Transactional
    public List<D> saveAll(List<D> domains) {
        Iterable<E> documents = this.documentMapper.toDocumentList(domains);
        documents = this.repository.saveAll(documents);
        return this.documentMapper.toDomainModelList(documents);
    }
}
