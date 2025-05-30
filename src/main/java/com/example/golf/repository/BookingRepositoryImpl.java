package com.example.golf.repository;

import com.example.golf.dtos.booking.Request.BookingSearchRequest;
import com.example.golf.dtos.booking.Response.BookingResponse;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.model.Booking;
import com.example.golf.service.impl.BookingServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BookingRepositoryImpl {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    @Lazy
    private BookingServiceImpl bookingServiceImpl;

    public BaseSearchResponse<BookingResponse> searchBooking(BookingSearchRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Create the query and root for Booking
        CriteriaQuery<Booking> query = cb.createQuery(Booking.class);
        Root<Booking> root = query.from(Booking.class);

        // Create predicates for filters
        List<Predicate> predicates = buildPredicates(cb, root, request);

        // Apply predicates to the query
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // Sorting
        applySorting(cb, query, root, request);

        // Pagination
        int page = request.getPage();
        int size = request.getSize();
        TypedQuery<Booking> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(page * size);
        typedQuery.setMaxResults(size);

        // Get result list
        List<Booking> bookings = typedQuery.getResultList();
        List<BookingResponse> data = bookings.stream()
                .map(booking -> bookingServiceImpl.convertToResponse(booking, BookingResponse.class))
                .toList();

        // Count query to get total number of elements
        Long totalElements = getTotalCount(cb, request);

        // Pagination metadata
        BaseSearchResponse.Pagination pagination = new BaseSearchResponse.Pagination(
                page, size, totalElements,
                (int) Math.ceil((double) totalElements / size)
        );

        return new BaseSearchResponse<>(data, pagination);
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Booking> root, BookingSearchRequest request) {
        List<Predicate> predicates = new ArrayList<>();

        // Filter by keyword1 and keyword2
        log.info("request.getKeyword() = {}, {}", request.getKey(), request.getValue());
        if (StringUtils.hasText(request.getKey()) && StringUtils.hasText(request.getValue())) {
            predicates.add(cb.like(cb.lower(root.get(request.getKey())), "%" + request.getValue().toLowerCase() + "%"));
        }

        if (StringUtils.hasText(request.getKey2()) && StringUtils.hasText(request.getValue2())) {
            predicates.add(cb.like(cb.lower(root.get(request.getKey2())), "%" + request.getValue2().toLowerCase() + "%"));
        }

        // Filter by bookingCode
        if (StringUtils.hasText(request.getBookingCode())) {
            predicates.add(cb.like(cb.lower(root.get("bookingCode")), "%" + request.getBookingCode().toLowerCase() + "%"));
        }

        // Filter by phone
        if (StringUtils.hasText(request.getPhone())) {
            predicates.add(cb.like(cb.lower(root.get("phone")), "%" + request.getPhone().toLowerCase() + "%"));
        }

        // Filter by golfCourseId
        if (StringUtils.hasText(request.getGolfCourseId())) {
            predicates.add(cb.equal(root.get("golfCourseId"), request.getGolfCourseId()));
        }

        // Filter by bookingDate
        if (request.getBookingDate() != null) {
            predicates.add(cb.equal(root.get("bookingDate"), request.getBookingDate()));
        }

        // Filter by status
        if (request.getStatus() != null && !request.getStatus().toString().isEmpty()) {
            predicates.add(cb.equal(root.get("status"), request.getStatus()));
        }

        // Filter by userId
        if (StringUtils.hasText(request.getUserId())) {
            predicates.add(cb.equal(root.get("userId"), request.getUserId()));
        }

        // Filter by createdAt (startDate, endDate)
        if (request.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.getStartDate().atStartOfDay()));
        }
        if (request.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), request.getEndDate().atTime(23, 59, 59)));
        }

        return predicates;
    }

    private void applySorting(CriteriaBuilder cb, CriteriaQuery<Booking> query, Root<Booking> root, BookingSearchRequest request) {
        // Sorting by the requested field
        Path<?> sortPath;
        try {
            sortPath = root.get(request.getSortBy());
        } catch (IllegalArgumentException e) {
            sortPath = root.get("createdAt"); // Fallback to createdAt if sortBy is invalid
        }
        Sort.Direction direction = Sort.Direction.fromOptionalString(request.getSortDir()).orElse(Sort.Direction.DESC);
        query.orderBy(direction.isAscending() ? cb.asc(sortPath) : cb.desc(sortPath));
    }

    private Long getTotalCount(CriteriaBuilder cb, BookingSearchRequest request) {
        // Count query for total elements
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Booking> countRoot = countQuery.from(Booking.class);
        countQuery.select(cb.count(countRoot));

        // Apply filters to count query
        List<Predicate> countPredicates = buildPredicates(cb, countRoot, request);
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
