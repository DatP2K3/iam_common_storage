package com.evotek.elasticsearch.application.service.impl.query;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.evotek.elasticsearch.application.dto.request.SearchUserRequest;
import com.evotek.elasticsearch.application.dto.response.SearchUserResponse;
import com.evotek.elasticsearch.application.service.impl.UserQueryService;
import com.evotek.elasticsearch.infrastructure.persistence.document.UserDocument;
import com.evotek.elasticsearch.infrastructure.persistence.mapper.UserDocumentMapper;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final ElasticsearchOperations elasticsearchOperations;
    private final UserDocumentMapper userDocumentMapper;

    @Override
    public SearchUserResponse searchUser(SearchUserRequest request) {
        Query query = Query.of(q -> q.bool(boolBuilder -> {
            // Tìm kiếm theo searchTerm nếu có
            if (StringUtils.hasText(request.getKeyword())) {
                // Thêm điều kiện tìm kiếm cho username với trọng số cao
                boolBuilder.should(shouldQuery -> shouldQuery.matchPhrasePrefix(matchQuery ->
                        matchQuery.field("username").query(request.getKeyword()).boost(2.0f)));

                boolBuilder.should(shouldQuery -> shouldQuery.matchPhrasePrefix(matchQuery ->
                        matchQuery.field("email").query(request.getKeyword()).boost(1.5f)));

                // Đảm bảo ít nhất một trong các điều kiện should phải được thỏa mãn
                boolBuilder.minimumShouldMatch("1");
            }

            // Lọc theo role nếu có
            if (request.getRoleId() != null) {
                boolBuilder.filter(filterQuery -> filterQuery.term(termQuery ->
                        termQuery.field("roleId").value(request.getRoleId().toString())));
            }

            // Lọc theo trạng thái locked nếu có
            if (request.getLocked() != null) {
                boolBuilder.filter(filterQuery ->
                        filterQuery.term(termQuery -> termQuery.field("locked").value(request.getLocked())));
            }
            // Lọc theo số năm kinh nghiệm
            if (request.getMinExperience() != null || request.getMaxExperience() != null) {
                boolBuilder.filter(filterQuery -> filterQuery.range(rangeQuery -> {
                    rangeQuery.field("yearsOfExperience");

                    if (request.getMinExperience() != null) {
                        // Chuyển đổi Integer thành JsonData
                        rangeQuery.gte(JsonData.of(request.getMinExperience()));
                    }

                    if (request.getMaxExperience() != null) {
                        // Chuyển đổi Integer thành JsonData
                        rangeQuery.lte(JsonData.of(request.getMaxExperience()));
                    }

                    return rangeQuery;
                }));
            }

            return boolBuilder;
        }));

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(query)
                .withPageable(PageRequest.of(
                        request.getPage(),
                        request.getSize(),
                        Sort.by(
                                request.getSortDirection().equalsIgnoreCase("desc")
                                        ? Sort.Direction.DESC
                                        : Sort.Direction.ASC,
                                request.getSortField())))
                .build();

        // Thực hiện tìm kiếm®
        SearchHits<UserDocument> searchHits = elasticsearchOperations.search(searchQuery, UserDocument.class);

        // Xử lý kết quả
        List<UserDocument> userDocuments =
                searchHits.getSearchHits().stream().map(SearchHit::getContent).toList();

        // Tạo và trả về response
        return SearchUserResponse.builder()
                .users(userDocumentMapper.toDomainModelList(userDocuments))
                .totalElements(searchHits.getTotalHits())
                .totalPages((int) Math.ceil((double) searchHits.getTotalHits() / request.getSize()))
                .pageIndex(request.getPage())
                .hasNext((request.getPage() * 1L) * request.getSize() < searchHits.getTotalHits())
                .hasPrevious(request.getPage() > 0)
                .build();
    }
}
//từ file jarr viết docker file coppy file jarr vào image, viết docker compose(định nghĩa build)
//c2: viết luôn trong docker file