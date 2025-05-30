package com.example.golf.dtos.membership.request;

import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.enums.BookingStatus;
import com.example.golf.enums.MembershipStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MembershipSearchRequest extends BaseSearchRequest {
   private String type;
   private String status;
}
