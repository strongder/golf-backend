package com.example.golf.dtos.payment.request;

import com.example.golf.dtos.search.BaseSearchRequest;
import lombok.Data;

@Data
public class PaymentSearchRequest extends BaseSearchRequest {
    private String type;
    private String code;
}
