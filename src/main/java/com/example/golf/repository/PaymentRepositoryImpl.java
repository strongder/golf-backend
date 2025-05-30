package com.example.golf.repository;

import com.example.golf.dtos.payment.request.PaymentSearchRequest;
import com.example.golf.dtos.payment.response.PaymentResponse;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.dtos.user.Response.DataFieldUser;
import com.example.golf.enums.PaymentStatus;
import com.example.golf.enums.PaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepositoryImpl {
    @Autowired
    private  JdbcTemplate jdbcTemplate;
    public BaseSearchResponse<PaymentResponse> searchPayment(PaymentSearchRequest request) {
        int page = Optional.ofNullable(request.getPage()).orElse(1);
        int size = Optional.ofNullable(request.getSize()).orElse(10);
        int offset = (page - 1) * size;

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("""
                    SELECT p.id, p.amount, p.user_id, p.transaction_id, p.payment_method,
                           p.status, p.type, p.reference_id, p.created_at,
                           g.email, g.phone, g.full_name,
                           b.booking_code AS booking_code,
                           m.code AS membership_code
                    FROM payment p
                    JOIN guest g ON p.user_id = g.id
                    LEFT JOIN booking b ON p.type = 'BOOKING' AND p.reference_id = b.id
                    LEFT JOIN membership m ON p.type = 'MEMBERSHIP' AND p.reference_id = m.id
                    WHERE 1=1
                """);

        // Lọc theo key-value (email, phone, fullName)
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
                case "bookingCode":
                    sql.append(" AND LOWER(b.code) LIKE ? ");
                    params.add("%" + request.getValue().toLowerCase() + "%");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown search key: " + request.getKey());
            }
        }

        // Lọc theo status (PENDING, COMPLETED, FAILED)
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            sql.append(" AND p.status = ? ");
            params.add(request.getStatus());
        }

        // Lọc theo type (BOOKING, MEMBERSHIP, SERVICE)
        if (request.getType() != null && !request.getType().isEmpty()) {
            sql.append(" AND p.type = ? ");
            params.add(request.getType());
        }
        // loc tu startDate to endDate
        if (request.getStartDate() != null && request.getEndDate() != null) {
            sql.append(" AND p.created_at BETWEEN ? AND ? ");
            params.add(request.getStartDate());
            params.add(request.getEndDate());
        } else if (request.getStartDate() != null) {
            sql.append(" AND p.created_at >= ? ");
            params.add(request.getStartDate());
        } else if (request.getEndDate() != null) {
            sql.append(" AND p.created_at <= ? ");
            params.add(request.getEndDate());
        }


        // Count query
        String countSql = "SELECT COUNT(*) FROM (" + sql + ") AS total";
        long totalElements = jdbcTemplate.queryForObject(countSql, params.toArray(), Long.class);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        // Pagination + Order
        sql.append(" ORDER BY p.created_at DESC LIMIT ? OFFSET ? ");
        params.add(size);
        params.add(offset);

        List<PaymentResponse> results = jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> {
            PaymentResponse p = new PaymentResponse();
            p.setId(rs.getString("id"));
            p.setAmount(rs.getDouble("amount"));
            p.setUserId(rs.getString("user_id"));
            p.setTransactionId(rs.getString("transaction_id"));
            p.setPaymentMethod(rs.getString("payment_method"));
            p.setStatus(PaymentStatus.valueOf(rs.getString("status")));
            p.setType(PaymentType.valueOf(rs.getString("type")));
            p.setReferenceId(rs.getString("reference_id"));
            p.setCreatedAt(rs.getDate("created_at").toLocalDate());

            // User field (email, phone, full name)
            DataFieldUser user = new DataFieldUser();
            user.setEmail(rs.getString("email"));
            user.setPhone(rs.getString("phone"));
            user.setFullName(rs.getString("full_name"));
            p.setUser(user);

            return p;
        });

        BaseSearchResponse.Pagination pagination = new BaseSearchResponse.Pagination(
                page, size, totalElements, totalPages
        );

        BaseSearchResponse<PaymentResponse> response = new BaseSearchResponse<>();
        response.setData(results);
        response.setPagination(pagination);

        return response;
    }


}
