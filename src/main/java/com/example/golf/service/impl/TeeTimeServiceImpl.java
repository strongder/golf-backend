package com.example.golf.service.impl;

import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.dtos.tee_time.response.TeeTimeRequest;
import com.example.golf.dtos.tee_time.response.TeeTimeResponse;
import com.example.golf.enums.ErrorResponse;
import com.example.golf.enums.TeeTimeStatus;
import com.example.golf.enums.UserRole;
import com.example.golf.exception.AppException;
import com.example.golf.model.GolfCourse;
import com.example.golf.model.TeeTime;
import com.example.golf.model.TeeTimeConfig;
import com.example.golf.model.User;
import com.example.golf.repository.GolfCourseRepository;
import com.example.golf.repository.TeeTimeConfigRepository;
import com.example.golf.repository.TeeTimeRepository;
import com.example.golf.service.TeeTimeService;
import com.example.golf.utils.SearchUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
public class TeeTimeServiceImpl extends BaseServiceImpl<TeeTime, String> implements TeeTimeService {

    private final TeeTimeConfigRepository teeTimeConfigRepository;
    public TeeTimeRepository teeTimeRepository;
    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    private GolfCourseRepository golfCourseRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private SearchUtils<TeeTime> searchUtils;

    public TeeTimeServiceImpl(TeeTimeRepository teeTimeRepository, TeeTimeConfigRepository teeTimeConfigRepository) {
        super(teeTimeRepository);
        this.teeTimeRepository = teeTimeRepository;
        this.teeTimeConfigRepository = teeTimeConfigRepository;
    }
    @Scheduled(cron = "0 0,30 * * * *")
    public void autoCreateTeeTimeRolling() {
        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            if(!teeTimeRepository.existsByDate(date))
            {
                String dayType = getDayType(date); // WEEKDAY hoặc WEEKEND
                List<TeeTimeConfig> configs = teeTimeConfigRepository.findByDateType(dayType);
                configs.forEach(config -> generateTeeTimeFromConfig(config, date));
            }
        }
    }

    public void generateTeeTimeFromConfig(TeeTimeConfig teeTimeConfig, LocalDate date) {
        GolfCourse golfCourse = golfCourseRepository.findById(teeTimeConfig.getGolfCourseId()).orElseThrow(() -> new RuntimeException("Golf course not found"));
        LocalTime startTime = teeTimeConfig.getStartTime();
        LocalTime endTime = teeTimeConfig.getEndTime();
        Duration slot = Duration.ofMinutes(teeTimeConfig.getDuration());

        while (!startTime.isAfter(endTime.minus(slot))) {
            TeeTime teeTime = new TeeTime();
            teeTime.setDate(date);
            teeTime.setStartTime(startTime);
            teeTime.setEndTime(startTime.plusMinutes(golfCourse.getDuration()));
            teeTime.setGolfCourseId(teeTimeConfig.getGolfCourseId());
            teeTime.setStatus(TeeTimeStatus.AVAILABLE);
            teeTime.setPrice(teeTimeConfig.getPrice());
            teeTime.setMaxPlayers(teeTimeConfig.getMaxPlayers());
            teeTimeRepository.save(teeTime);
            startTime = startTime.plus(slot);
        }
    }
    public String getDayType(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return "WEEKEND";
        } else {
            return "WEEKDAY";
        }
    }

    @Transactional(readOnly = true)
    public List<TeeTimeResponse> getAvailableTeeTimes(String golfCourseId, LocalDate date) {
        List<TeeTime> teeTimes =  teeTimeRepository.findTeeTimesByDateAndStatus(golfCourseId, date, TeeTimeStatus.AVAILABLE);
        return teeTimes.stream()
                .map(teeTime -> modelMapper.map(teeTime, TeeTimeResponse.class))
                .toList();
    }


    //phan trang voi pageable
    @Override
    @Transactional(readOnly = true)
    public List<TeeTimeResponse> getTeeTimeByDateAndGolfCourseId(String golfCourseId, LocalDate date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        List<TeeTime> teeTimes = teeTimeRepository.findTeeTimesByDateAndGolfCourseId(golfCourseId, date, pageable);
        return teeTimes.stream()
                .map(teeTime -> modelMapper.map(teeTime, TeeTimeResponse.class))
                .toList();
    }


    @Transactional
    @Override
    public BaseSearchResponse<TeeTimeResponse> search(BaseSearchRequest request)
    {
        return searchUtils.search(TeeTime.class, request, teeTime -> convertToResponse(teeTime, TeeTimeResponse.class));
    }

    //update
    @Transactional
    @Override
    public TeeTimeResponse updateTeeTime(String id, TeeTimeRequest teeTimeRequest) {
        TeeTime existingTeeTime = teeTimeRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        modelMapper.map(teeTimeRequest, existingTeeTime);
        existingTeeTime.setId(id);
        teeTimeRepository.save(existingTeeTime);
        return modelMapper.map(existingTeeTime, TeeTimeResponse.class);
    }

    @Transactional
    public String softDeleteTeeTime(String id) {
        TeeTime teeTime = teeTimeRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        teeTime.setDeleted(true);
        teeTimeRepository.save(teeTime);
        return id;
    }


    //xử lý giữ tee time
    @Transactional
    public TeeTimeResponse holdTeeTime(String teeTimeId, int numberOfHoles) {
        User currentUser = userServiceImpl.getCurrentUser();

        TeeTime firstTeeTime = teeTimeRepository.findById(teeTimeId)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));

        // Kiểm tra thời gian hiện tại
        LocalDateTime teeTimeStart = LocalDateTime.of(firstTeeTime.getDate(), firstTeeTime.getStartTime());
        if (teeTimeStart.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot hold a tee time that is in the past");
        }
        if (firstTeeTime.getStatus() != TeeTimeStatus.AVAILABLE) {
            throw new RuntimeException("Cannot hold a tee time that is not available");
        }

        GolfCourse golfCourse = golfCourseRepository.findById(firstTeeTime.getGolfCourseId())
                .orElseThrow(() -> new RuntimeException("Golf course not found"));
        int holesPerRound = golfCourse.getHoles();
        int numRounds = numberOfHoles / holesPerRound;

        // Danh sách các tee time cần giữ
        List<TeeTime> teeTimesToHold = new ArrayList<>();
        teeTimesToHold.add(firstTeeTime);

        LocalTime currentStartTime = firstTeeTime.getStartTime();

        for (int i = 1; i < numRounds; i++) {
            LocalTime nextStartTime = currentStartTime.plusMinutes(golfCourse.getDuration());
            Optional<TeeTime> nextTeeTimeOpt = teeTimeRepository.findTeeTimeNext(
                    golfCourse.getId(),
                    firstTeeTime.getDate(),
                    nextStartTime,
                    TeeTimeStatus.AVAILABLE
            );

            if (nextTeeTimeOpt.isEmpty()) {
                throw new RuntimeException("Cannot hold all required tee times (next tee time unavailable)");
            }
            TeeTime nextTeeTime = nextTeeTimeOpt.get();
            teeTimesToHold.add(nextTeeTime);// Thêm tee time kế tiếp vào danh sách
            currentStartTime = nextTeeTime.getStartTime();
        }
        // Đánh dấu HOLD tất cả teeTime
        String heldBy = currentUser.getRole() != UserRole.MEMBER ? "STAFF_" + currentUser.getId() : currentUser.getId();
        LocalDateTime now = LocalDateTime.now();
        for (TeeTime teeTime : teeTimesToHold) {
            teeTime.setStatus(TeeTimeStatus.HOLD);
            teeTime.setHeldAt(now);
            teeTime.setHeldBy(heldBy);
            teeTimeRepository.save(teeTime);
        }
        return convertToResponse(firstTeeTime, TeeTimeResponse.class);
    }

    @Scheduled(fixedDelay = 60 * 1000) // chạy mỗi 5 phút
    @Transactional
    public void releaseExpiredHeldTeeTimes() {
        // check xem nhung cai nao available nhung qua han thi chuyen thanh unavailable
        // Lấy thời gian hiện tại
        List<TeeTime> availableTeeTimes = teeTimeRepository.findByStatusAndDate(TeeTimeStatus.AVAILABLE, LocalDate.now());
        availableTeeTimes.forEach(
                teeTime -> {
                    if (teeTime.getStartTime().isBefore(LocalTime.now())) {
                        teeTime.setStatus(TeeTimeStatus.UNAVAILABLE);
                        teeTimeRepository.save(teeTime);
                    }
                }
        );
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(5);
        // Lấy danh sách teeTime đang HOLD và holdAt < expiredTime
        List<TeeTime> expiredHeldTeeTimes = teeTimeRepository.findByStatusAndHeldAtBefore(TeeTimeStatus.HOLD, expiredTime);

        for (TeeTime teeTime : expiredHeldTeeTimes) {
            teeTime.setStatus(TeeTimeStatus.AVAILABLE);
            teeTime.setHeldAt(null);
            teeTime.setHeldBy(null);
            teeTimeRepository.save(teeTime);
        }
    }

    @Override
    protected <Req> TeeTime convertToEntity(Req request) {
        return modelMapper.map(request, TeeTime.class);
    }

    @Override
    protected <Res> Res convertToResponse(TeeTime entity, Class<Res> responseType) {
        return  modelMapper.map(entity, responseType);
    }
}
