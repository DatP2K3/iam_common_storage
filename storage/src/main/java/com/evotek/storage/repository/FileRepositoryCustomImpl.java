package com.evotek.storage.repository;


import com.evotek.storage.dto.request.FileSearchRequest;
import com.evotek.storage.model.File;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileRepositoryCustomImpl implements FileRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<File> search(FileSearchRequest fileSearchRequest) {
        Map<String, Object> values = new HashMap<>();
        String sql = "select f from File f " + createWhereQuery(fileSearchRequest.getKeyword(), values) + createOrderQuery(fileSearchRequest.getSortBy());
        Query query = entityManager.createQuery(sql, File.class);
        values.forEach(query::setParameter);
        query.setFirstResult((fileSearchRequest.getPageIndex() - 1) * fileSearchRequest.getPageSize());
        query.setMaxResults(fileSearchRequest.getPageSize());
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
}
