package com.example.golf.utils;

import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SearchUtils<T> {
    @PersistenceContext
    private EntityManager entityManager;

    public <R> BaseSearchResponse<R> search(Class<T> entityClass, BaseSearchRequest searchRequest, Function<T, R> converter) {
        log.info("Searching for {} with request: {}", entityClass.getSimpleName(), searchRequest);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);

        // Tạo danh sách điều kiện tìm kiếm
        List<Predicate> predicates = buildPredicates(cb, root, searchRequest);
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // Sắp xếp
        Sort.Direction direction = Sort.Direction.fromString(searchRequest.getSortDir());
        query.orderBy(direction == Sort.Direction.ASC
                ? cb.asc(root.get(searchRequest.getSortBy()))
                : cb.desc(root.get(searchRequest.getSortBy()))
        );

        // Phân trang
        int page = (searchRequest.getPage() != null ? searchRequest.getPage() : 1) - 1;
        int size = searchRequest.getSize() != null ? searchRequest.getSize() : 10;

        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);

        List<T> results = typedQuery.getResultList();
        List<R> transformedResults = results.stream().map(converter).collect(Collectors.toList());

        // Tổng số bản ghi
        long total = countTotal(entityClass, searchRequest);
        int totalPages = (int) Math.ceil((double) total / size);

        BaseSearchResponse.Pagination pagination = new BaseSearchResponse.Pagination(
                page,
                size,
                total,
                totalPages
        );

        return new BaseSearchResponse<>(transformedResults, pagination);
    }

    private long countTotal(Class<T> entityClass, BaseSearchRequest searchRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> root = countQuery.from(entityClass);

        List<Predicate> predicates = buildPredicates(cb, root, searchRequest);
        countQuery.select(cb.count(root)).where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<T> root, BaseSearchRequest searchRequest) {
        List<Predicate> predicates = new ArrayList<>();

        // Tìm kiếm theo key & value
        if (StringUtils.hasText(searchRequest.getKey()) && StringUtils.hasText(searchRequest.getValue())) {
            predicates.add(cb.like(root.get(searchRequest.getKey()), "%" + searchRequest.getValue() + "%"));
        }

        // Tìm kiếm theo key2 & value2
        if (StringUtils.hasText(searchRequest.getKey2()) && StringUtils.hasText(searchRequest.getValue2())) {
            predicates.add(cb.like(root.get(searchRequest.getKey2()), "%" + searchRequest.getValue2() + "%"));
        }

        // Lọc theo status
        if (StringUtils.hasText(searchRequest.getStatus())) {
            predicates.add(cb.equal(root.get("status"), searchRequest.getStatus()));
        }

        // Lọc theo ngày tạo từ startDate đến endDate
        if (searchRequest.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), searchRequest.getStartDate().atStartOfDay()));
        }

        if (searchRequest.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), searchRequest.getEndDate().atTime(23, 59, 59)));
        }

        // Lọc theo date cụ thể
        if (searchRequest.getDate() != null) {
            predicates.add(cb.equal(root.get("date"), searchRequest.getDate()));
        }

        // Luôn lọc theo isDeleted = false
        predicates.add(cb.equal(root.get("isDeleted"), false));

        return predicates;
    }
}
