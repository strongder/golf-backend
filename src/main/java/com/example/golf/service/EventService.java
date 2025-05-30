package com.example.golf.service;

import com.example.golf.dtos.event.request.CreateEventRequest;
import com.example.golf.dtos.event.request.EventForUserRequest;
import com.example.golf.dtos.event.response.EventResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventService extends BaseService<Event, String> {
    String softDelete(String id);
     List<EventResponse> getEventByTypeAndDate(EventForUserRequest eventForUserRequest);


    Object search(BaseSearchRequest request);

    EventResponse update(String id, CreateEventRequest request);
}
