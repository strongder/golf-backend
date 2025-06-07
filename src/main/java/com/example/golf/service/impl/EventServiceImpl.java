package com.example.golf.service.impl;

import com.example.golf.dtos.event.request.CreateEventRequest;
import com.example.golf.dtos.event.request.EventForUserRequest;
import com.example.golf.dtos.event.response.EventResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.enums.ErrorResponse;
import com.example.golf.exception.AppException;
import com.example.golf.model.Event;
import com.example.golf.model.GolfCourse;
import com.example.golf.model.User;
import com.example.golf.repository.EventRepository;
import com.example.golf.service.EventService;
import com.example.golf.service.GolfCourseService;
import com.example.golf.service.UserService;
import com.example.golf.utils.SearchUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@EnableScheduling
public class EventServiceImpl extends BaseServiceImpl<Event, String> implements EventService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    private GolfCourseService golfCourseSErvice;
    @Autowired
    private UserService userService;
    @Autowired
    private SearchUtils<Event> searchUtils;
    @Autowired
    private FileService fileService;

    public EventServiceImpl(EventRepository eventRepository) {
        super(eventRepository);
        this.eventRepository = eventRepository;
    }

    @Scheduled(cron = "0 5 0 * * ?") // Chạy mỗi ngày lúc 00:05
    @Transactional
    public void updateEventStatus() {
        List<Event> events = eventRepository.findByDeletedFalse();
        LocalDate today = LocalDate.now();

        for (Event event : events) {
            if (today.isEqual(event.getStartDate())) {
                event.setStatus("ACTIVE");
            } else if (today.isAfter(event.getEndDate())) {
                event.setStatus("INACTIVE");
            } else if (today.isBefore(event.getStartDate())) {
                event.setStatus("UPCOMING");
            }
        }
        eventRepository.saveAll(events); // Đừng quên lưu lại!
    }

    @Override
    protected <Req> Event convertToEntity(Req request) {
        return modelMapper.map(request, Event.class);
    }

    @Override
    protected <Res> Res convertToResponse(Event entity, Class<Res> responseType) {
        EventResponse eventResponse = modelMapper.map(entity, EventResponse.class);
        return responseType.cast(eventResponse);
    }

    @Override
    public String softDelete(String id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        event.setDeleted(true);
        eventRepository.save(event);
        return id;
    }

    @Override
    @Transactional
    public List<EventResponse> getEventByTypeAndDate(EventForUserRequest eventForUserRequest) {
        User user = userService.getCurrentUser();
        String role = user.getRole().name();
        List<Event> events = eventRepository.findEventForUser(eventForUserRequest.getType(), eventForUserRequest.getDate(), eventForUserRequest.getStatus(), role);
        return events.stream()
                .map(event -> modelMapper.map(event, EventResponse.class))
                .toList();
    }

    @Transactional
    public BaseSearchResponse<EventResponse> search(BaseSearchRequest request) {
        return searchUtils.search(Event.class, request, event -> convertToResponse(event, EventResponse.class));
    }

    @Override
    public EventResponse create(CreateEventRequest request) {
        Event event = convertToEntity(request);
        event.setStatus(request.getStatus());
        event.setDeleted(false);
        event = eventRepository.save(event);
        if(request.getImage() != null) {
            uploadFileAsync(request.getImage(), event);
        }
        return convertToResponse(event, EventResponse.class);
    }
    @Transactional
    public EventResponse update(String id, CreateEventRequest request)
    {
        Event event = eventRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        modelMapper.map(request, event);
        event.setId(id);
        eventRepository.save(event);
        if (request.getImage() != null) {
            uploadFileAsync(request.getImage(), event);
        }
        return modelMapper.map(event, EventResponse.class);
    }
    @Async
    public void uploadFileAsync(MultipartFile file, Event event) {
        try {
            String fileName = fileService.uploadFile(file); // upload file bình thường
            event.setImageUrl(fileName);
            eventRepository.save(event);
        } catch (IOException e) {
            // log error
        }
    }
}
