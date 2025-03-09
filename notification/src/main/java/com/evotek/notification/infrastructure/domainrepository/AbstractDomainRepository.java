package com.evotek.storage.infrastructure.domainrepository;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evotek.storage.infrastructure.persistence.mapper.EntityMapper;

public abstract class AbstractDomainRepository<D, E, ID> implements DomainRepository<D, ID> {
    protected final JpaRepository<E, ID> repository;
    protected final EntityMapper<D, E> entityMapper;

    protected AbstractDomainRepository(JpaRepository<E, ID> repository, EntityMapper<D, E> entityMapper) {
        this.repository = repository;
        this.entityMapper = entityMapper;
    }

    @Override
    public Optional<D> findById(ID id) {
        Optional<E> entity = repository.findById(id);
        return entity.map(entityMapper::toDomainModel);
    }

    @Override
    @Transactional
    public D save(D domainModel) {
        List<D> domainModels = this.saveAll(List.of(domainModel));
        return domainModels.getFirst();
    }

    @Override
    public List<D> findAllByIds(List<ID> ids) {
        return this.repository.findAllById(ids).stream()
                .map(this.entityMapper::toDomainModel)
                .toList();
    }

    @Override
    @Transactional
    public List<D> saveAll(List<D> domains) {
        List<E> entities = this.entityMapper.toEntityList(domains);
        entities = this.repository.saveAll(entities);
        return this.entityMapper.toDomainModelList(entities);
    }
}
