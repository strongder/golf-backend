package com.example.golf.service;

import com.example.golf.dtos.event.request.CreateEventRequest;
import com.example.golf.dtos.event.request.EventForUserRequest;
import com.example.golf.dtos.event.response.EventResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.model.Event;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface EventService extends BaseService<Event, String> {
    String softDelete(String id);
    List<EventResponse> getEventByTypeAndDate(EventForUserRequest eventForUserRequest);
    Object search(BaseSearchRequest request);
    EventResponse create(CreateEventRequest request);
    EventResponse update(String id, CreateEventRequest request);

    // lay ra su kien khuyen mai cho user hien tai de gan vao khi booking(lay ra khuyen mai co discount lon nhat)
    @Transactional
    EventResponse getPromotionEventsForUser(String customId);
}
