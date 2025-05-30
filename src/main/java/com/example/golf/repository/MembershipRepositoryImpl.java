package com.example.golf.repository;

import com.example.golf.dtos.membership.request.MembershipSearchRequest;
import com.example.golf.dtos.membership.response.MembershipResponse;
import com.example.golf.dtos.search.BaseSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MembershipRepositoryImpl {

    private final JdbcTemplate jdbcTemplate;

    public BaseSearchResponse<MembershipResponse> searchMembership(MembershipSearchRequest request) {
        int page = Optional.ofNullable(request.getPage()).orElse(1);
        int size = Optional.ofNullable(request.getSize()).orElse(10);
        int offset = (page - 1) * size;

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("""
            SELECT m.id, m.code, m.user_id, m.membership_type_id, m.start_date, m.end_date, m.status,
                   u.email, u.phone, u.full_name, mt.name AS type_name
            FROM membership m
            JOIN user u ON m.user_id = u.id
            JOIN membership_type mt ON m.membership_type_id = mt.id
            WHERE 1=1
        """);

        // filter by key and value

        if (StringUtils.hasText(request.getKey()) && StringUtils.hasText(request.getValue())) {
            switch (request.getKey()) {
                case "email":
                    sql.append(" AND LOWER(u.email) LIKE ? ");
                    params.add("%" + request.getValue().toLowerCase() + "%");
                    break;
                case "phone":
                    sql.append(" AND u.phone LIKE ? ");
                    params.add("%" + request.getValue() + "%");
                    break;
                case "fullName":
                    sql.append(" AND LOWER(u.full_name) LIKE ? ");
                    params.add("%" + request.getValue().toLowerCase() + "%");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown search key: " + request.getKey());
            }
        }
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            sql.append(" AND m.status = ? ");
            params.add(request.getStatus());
        }
        if (request.getType() != null && !request.getType().isEmpty()) {
            sql.append(" AND mt.name = ? ");
            params.add(request.getType());
        }

        // Count query
        String countSql = "SELECT COUNT(*) FROM (" + sql + ") AS total";
        long totalElements = jdbcTemplate.queryForObject(countSql, params.toArray(), Long.class);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        // Pagination + Order
        sql.append(" ORDER BY m.start_date DESC LIMIT ? OFFSET ? ");
        params.add(size);
        params.add(offset);

        List<MembershipResponse> results = jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> {
            MembershipResponse m = new MembershipResponse();
            m.setId(rs.getString("id"));
            m.setUserId(rs.getString("user_id"));
            m.setCode(rs.getString("code"));
            m.setMembershipTypeId(rs.getString("membership_type_id"));
            m.setStartDate(String.valueOf(rs.getDate("start_date").toLocalDate()));
            m.setEndDate(String.valueOf(rs.getDate("end_date").toLocalDate()));
            m.setEmail(rs.getString("email"));
            m.setPhone(rs.getString("phone"));
            m.setFullName(rs.getString("full_name"));
            m.setStatus(rs.getString("status"));
            m.setMembershipTypeName(rs.getString("type_name"));
            return m;
        });

        BaseSearchResponse.Pagination pagination = new BaseSearchResponse.Pagination(
                page, size, totalElements, totalPages
        );

        BaseSearchResponse<MembershipResponse> response = new BaseSearchResponse<>();
        response.setData(results);
        response.setPagination(pagination);

        return response;
    }
}
