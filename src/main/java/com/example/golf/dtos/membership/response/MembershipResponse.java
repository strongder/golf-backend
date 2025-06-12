package com.example.golf.dtos.membership.response;

import com.example.golf.dtos.membership_type.response.MembershipTypeResponse;
import com.example.golf.dtos.user.Response.DataFieldUser;
import lombok.Data;

@Data
public class MembershipResponse {
    private String id;
    private String userId;
    private String code;
    private String membershipTypeId;
    private String fullName;
    private double price;
    private double discount;
    private String email;
    private String phone;
    private String membershipTypeName;
    private String startDate;
    private String endDate;
    private String status;
    private String createdAt;
    private String updatedAt;
}
