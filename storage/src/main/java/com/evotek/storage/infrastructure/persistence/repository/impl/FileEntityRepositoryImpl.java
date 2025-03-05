package com.evotek.storage.infrastructure.persistence.repository.impl;


import com.evotek.storage.domain.query.SearchFileQuery;
import com.evotek.storage.infrastructure.persistence.entity.FileEntity;
import com.evotek.storage.infrastructure.persistence.repository.custom.FileEntityRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileEntityRepositoryImpl implements FileEntityRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FileEntity> search(SearchFileQuery searchFileQuery) {
        Map<String, Object> values = new HashMap<>();
        String sql = "select f from FileEntity f " + createWhereQuery(searchFileQuery.getKeyword(), values) + createOrderQuery(searchFileQuery.getSortBy());
        TypedQuery<FileEntity> query = entityManager.createQuery(sql, FileEntity.class);
        values.forEach(query::setParameter);
        query.setFirstResult((searchFileQuery.getPageIndex() - 1) * searchFileQuery.getPageSize());
        query.setMaxResults(searchFileQuery.getPageSize());
        return query.getResultList();
    }

    private String createWhereQuery(String keyword, Map<String, Object> values) {
        StringBuilder sql = new StringBuilder();
        if (!keyword.isBlank()) {
            sql.append(
                    " where ( lower(f.originName) like :keyword"
                            + " or lower(f.fileType) like :keyword)");
            values.put("keyword", encodeKeyword(keyword));
        }
        return sql.toString();
    }

    public StringBuilder createOrderQuery(String sortBy) {
        StringBuilder hql = new StringBuilder(" ");
        if (StringUtils.hasLength(sortBy)) {
            hql.append(" order by f.").append(sortBy.replace(".", " "));
        }
        return hql;
    }

    public String encodeKeyword(String keyword) {
        if (keyword == null) {
            return "%";
        }
        return "%" + keyword.trim().toLowerCase() + "%";
    }

    @Override
    public Long count(SearchFileQuery searchFileQuery) {
        Map<String, Object> values = new HashMap<>();
        String sql = "select count(f) from FileEntity f " + createWhereQuery(searchFileQuery.getKeyword(), values);
        Query query = entityManager.createQuery(sql, Long.class);
        values.forEach(query::setParameter);
        return (Long) query.getSingleResult();
    }
}
