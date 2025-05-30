package com.example.golf.service;

import com.example.golf.dtos.guest.request.UpdateGuestRequest;
import com.example.golf.dtos.guest.response.GuestResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.model.Guest;
import org.springframework.transaction.annotation.Transactional;

public interface GuestService extends BaseService<Guest, String> {
    BaseSearchResponse<GuestResponse> search(BaseSearchRequest request);
    String softDelete(String id);

    @Transactional
    GuestResponse updateGuest(String id, UpdateGuestRequest request);

    GuestResponse getGuestByUserId(String userId);
}
