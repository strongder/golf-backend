package com.example.golf.dtos.membership.request;


import lombok.Data;

@Data
public class CreateMembershipRequest {
    private String userId;
    private String membershipTypeId;
}
