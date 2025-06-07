package com.example.golf.service.impl;

import com.example.golf.dtos.booking.Request.*;
import com.example.golf.dtos.booking.Response.BookingResponse;
import com.example.golf.dtos.golf_course.response.DataFieldGolfCourse;
import com.example.golf.dtos.payment.request.PaymentRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.dtos.tee_time.response.DataFieldTeeTime;
import com.example.golf.enums.*;
import com.example.golf.exception.AppException;
import com.example.golf.model.*;
import com.example.golf.repository.*;
import com.example.golf.service.BookingDetailService;
import com.example.golf.service.BookingService;
import com.example.golf.service.ToolService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl extends BaseServiceImpl<Booking, String> implements BookingService {

    @Autowired
    public ModelMapper modelMapper;

    @Autowired
    public GolfCourseRepository golfCourseRepository;

    @Autowired
    public ToolService toolService;
    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    @Lazy
    private BookingRepositoryImpl searchCommon;
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private BookingDetailService bookingDetailService;
    @Autowired
    private TeeTimeRepository teeTimeRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    @Lazy
    private PaymentServiceImpl paymentServiceImpl;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        super(bookingRepository);
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {
        User currentUser = userService.getCurrentUser();
        GolfCourse golfCourse = golfCourseRepository.findById(request.getGolfCourseId())
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        Booking booking = convertToEntity(request);
        Guest guest = guestRepository.findGuestByFullNameAndPhone(request.getFullName(), request.getPhone())
                .orElseGet(() -> {
                    Guest newGuest = new Guest();
                    newGuest.setFullName(request.getFullName());
                    newGuest.setPhone(request.getPhone());
                    newGuest.setRole(GuestType.GUEST);
                    newGuest.setTotalBooking(1);
                    Guest saveGuest =  guestRepository.save(newGuest);
                    booking.setGolferId(saveGuest.getId());
                    return saveGuest;
                });

        if (guest.getId() != null) {
            guest.setTotalBooking(guest.getTotalBooking() + 1);
            guestRepository.save(guest);
        }
        booking.setBookingCode(generateBookingCode());
        booking.setStatus(BookingStatus.PENDING);
        TeeTime teeTime = teeTimeRepository.findById(booking.getTeeTimeId())
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        if (teeTime.getStatus() != TeeTimeStatus.HOLD) {
            throw new RuntimeException("Tee time is not being held");
        }

        // ✅ Kiểm tra thời gian HOLD còn hiệu lực (ví dụ: 5 phút)
        if (teeTime.getHeldAt() != null &&
                teeTime.getHeldAt().isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new RuntimeException("Tee time hold has expired");
        }


        String currentUserId = request.getUserId() != null ? request.getUserId() : "STAFF_" + currentUser.getId();

        if (teeTime.getHeldBy() != null && !teeTime.getHeldBy().equals(currentUserId)) {
            throw new RuntimeException("Tee time is held by another user");
        }

        // ✅ Cập nhật trạng thái thành BOOKED
        teeTime.setStatus(TeeTimeStatus.BOOKED);
        teeTime.setHeldAt(null);
        teeTime.setHeldBy(null);
        teeTime.setBookedPlayers(request.getNumPlayers());
        teeTimeRepository.save(teeTime);
        booking.setTeeTimeId(teeTime.getId());

        // Khởi tạo chi phí cơ bản từ tee time
        booking.setTotalCost(booking.getTotalCost() + teeTime.getPrice() * request.getNumPlayers());
        Booking savedBooking = bookingRepository.save(booking);
        // Xử lý số lỗ không khớp
        if (request.getNumberOfHoles() != golfCourse.getHoles()) {
            bookNextTeeTime(teeTime, golfCourse, savedBooking);
        }
        return convertToResponse(savedBooking, BookingResponse.class);
    }


    @Transactional
    @Override
    public BookingResponse updateBooking(String bookingId, UpdateBookingRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));

        booking.setPhone(request.getPhone());
        booking.setFullName(request.getFullName());
        booking.setEmail(request.getEmail());
        booking.setNote(request.getNote());
        booking.setNumPlayers(request.getNumPlayers());
        booking.setNumberOfHoles(request.getNumberOfHoles());
        booking.setBookingDate(request.getBookingDate());
        booking.setStatus(request.getStatus());

        TeeTime teeTime = teeTimeRepository.findById(request.getTeeTimeId())
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        teeTime.setBookedPlayers(request.getNumPlayers());
        teeTimeRepository.save(teeTime);

        booking.setTeeTimeId(request.getTeeTimeId());

        double teeTimeCost = teeTime.getPrice() * request.getNumPlayers();
        double serviceCost = bookingDetailRepository.sumTotalPriceByBookingId(bookingId);

        booking.setTotalCost(teeTimeCost + serviceCost);

        Booking updatedBooking = bookingRepository.save(booking);
        return convertToResponse(updatedBooking, BookingResponse.class);
    }


    public BaseSearchResponse<BookingResponse> search(BookingSearchRequest request) {
        return searchCommon.searchBooking(request);
    }

    @Transactional
    public String updateStatus(StatusBookingRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        if (!isValidStatusTransition(booking.getStatus(), request.getStatus())) {
            throw new AppException(ErrorResponse.INVALID_STATUS_TRANSITION);
        }
        booking.setStatus(request.getStatus());
        bookingRepository.save(booking);
        return request.getBookingId();
    }

    @Transactional
    public BookingResponse checkIn(String bookingCode) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        Booking booking = bookingRepository.findByBookingCode(bookingCode).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        booking.setStatus(BookingStatus.PLAYING);
        booking.setCheckInBy(user.getFullName());
        booking.setCheckInTime(LocalDateTime.now());
        TeeTime teeTime = teeTimeRepository.findById(booking.getTeeTimeId())
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        teeTime.setStatus(TeeTimeStatus.CHECKED_IN);
        teeTimeRepository.save(teeTime);
        bookingRepository.save(booking);
        List<BookingDetail> bookingDetails = bookingDetailRepository.findByBookingId(booking.getId());
        bookingDetails.forEach(bookingDetail -> {
            if (bookingDetail.getToolId() != null) {
                toolService.calcQuantity(bookingDetail.getToolId(), bookingDetail.getQuantity(), true);
            }
        });
        return convertToResponse(booking, BookingResponse.class);
    }

    @Transactional
    public BookingResponse checkOut(String bookingCode ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        Booking booking = bookingRepository.findById(bookingCode).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setCheckOutBy(user.getFullName());
        booking.setCheckOutTime(LocalDateTime.now());

        bookingRepository.save(booking);
        List<BookingDetail> bookingDetails = bookingDetailRepository.findByBookingId(booking.getId());
        bookingDetails.forEach(bookingDetail -> {
            if (bookingDetail.getToolId() != null) {
                toolService.calcQuantity(bookingDetail.getToolId(), bookingDetail.getQuantity(), false);
            }
        });
        TeeTime teeTime = teeTimeRepository.findById(booking.getTeeTimeId())
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        teeTime.setStatus(TeeTimeStatus.CHECKED_OUT);
        teeTimeRepository.save(teeTime);
        // tao payment
        Guest guest = guestRepository.findGuestByFullNameAndPhone(booking.getFullName(), booking.getPhone())
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setPaymentMethod(booking.getPaymentMethod());
        paymentRequest.setAmount(booking.getTotalCost() - booking.getDepositAmount());
        paymentRequest.setUserId(guest.getId());
        paymentRequest.setType(PaymentType.BOOKING + "");
        paymentRequest.setReferenceId(booking.getId());
        paymentRequest.setStatus(PaymentStatus.COMPLETED + "");
        paymentServiceImpl.createPayment(paymentRequest);
        return convertToResponse(booking, BookingResponse.class);
    }


    public boolean isValidStatusTransition(BookingStatus from, BookingStatus to) {
        return from == BookingStatus.PENDING && to == BookingStatus.CONFIRMED
                || from == BookingStatus.CONFIRMED && to == BookingStatus.PLAYING
                || from == BookingStatus.PLAYING && to == BookingStatus.COMPLETED
                || from == BookingStatus.PENDING && to == BookingStatus.CANCELED
                || from == BookingStatus.CONFIRMED && to == BookingStatus.CANCELED;
    }

    @Override
    protected Booking convertToEntity(Object request) {
        return modelMapper.map(request, Booking.class);
    }

    @Override
    public <Res> Res convertToResponse(Booking entity, Class<Res> responseType) {
        Res response = modelMapper.map(entity, responseType);
        if (response instanceof BookingResponse res) {
            res.setGolfCourse(modelMapper.map(
                    golfCourseRepository.findById(entity.getGolfCourseId()).orElse(new GolfCourse()),
                    DataFieldGolfCourse.class
            ));
            res.setTeeTime(modelMapper.map(
                    teeTimeRepository.findById(entity.getTeeTimeId()).orElse(new TeeTime()),
                    DataFieldTeeTime.class));

            res.setPriceByService(calculatorPriceService(entity));
            res.setPriceByCourse(calculatorPriceCourse(entity));
        }

        return response;
    }

    public void bookNextTeeTime(TeeTime teeTime, GolfCourse golfCourse, Booking booking) {
        int numRounds = booking.getNumberOfHoles() / golfCourse.getHoles(); // Ví dụ 18 / 9 = 2
        LocalDate bookingDate = teeTime.getDate();
        LocalTime startTime = teeTime.getStartTime();
        // Đánh dấu teeTime đầu tiên đã được đặt
        teeTime.setStatus(TeeTimeStatus.BOOKED);
        teeTime.setBookedPlayers(booking.getNumPlayers());
        teeTimeRepository.save(teeTime);
        for (int i = 1; i < numRounds; i++) {
            // Luôn chọn lại cùng một sân nếu chỉ có 1 sân hoặc chơi lại sân cũ
            GolfCourse nextGolfCourse = golfCourse;
            Optional<TeeTime> nextTeeTimeOpt = teeTimeRepository.findTeeTimeNext(
                    nextGolfCourse.getId(),
                    bookingDate,
                    startTime.plusMinutes(golfCourse.getDuration()), // Thời gian bắt đầu tee time tiếp theo
                    TeeTimeStatus.AVAILABLE
            );
            if (nextTeeTimeOpt.isEmpty()) {
                throw new AppException(ErrorResponse.TEE_TIME_NOT_AVAILABLE);
            }
            TeeTime nextTeeTime = nextTeeTimeOpt.get();
            nextTeeTime.setStatus(TeeTimeStatus.BOOKED);
            nextTeeTime.setBookedPlayers(booking.getNumPlayers());
            teeTimeRepository.save(nextTeeTime);
            // Cập nhật thời gian bắt đầu cho vòng tiếp theo (nếu có)
            startTime = nextTeeTime.getStartTime().plusMinutes(nextGolfCourse.getDuration());
        }
    }


    public String generateBookingCode() {
        return "GOLF-" + System.currentTimeMillis();
    }


    // viet api chạy moi 5p kiem tra booking xem cai nao qua thoi gian dat 5 phut con pending thi huy
    @Scheduled(fixedRate = 300000) // Chạy mỗi 5 phút
    public void cancelPendingBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> pendingBookings = bookingRepository.findAllByStatus(BookingStatus.PENDING);
        for (Booking booking : pendingBookings) {
            if (booking.getCreatedAt().plusMinutes(10).isBefore(now)) {
                booking.setStatus(BookingStatus.CANCELED);
                TeeTime teeTime = teeTimeRepository.findById(booking.getTeeTimeId())
                        .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
                teeTime.setStatus(TeeTimeStatus.AVAILABLE);
                teeTime.setBookedPlayers(0);
                teeTimeRepository.save(teeTime);
                bookingRepository.save(booking);
            }
        }
    }

    // tinh toan chi phi khi co khuyen mai hoac la membership
    public double calculateTotalCost(Booking booking) {
        double totalCost = 0.0;
        List<BookingDetail> bookingDetails = bookingDetailRepository.findByBookingId(booking.getId());
        for (BookingDetail bookingDetail : bookingDetails) {
            totalCost += bookingDetail.getTotalPrice();
        }
        return totalCost;
    }


    public String softDelete(String id)
    {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        booking.setDeleted(true);
        bookingRepository.save(booking);
        return id;
    }

    public void changeStatus(String bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        booking.setStatus(status);
        bookingRepository.save(booking);
    }

    public double calculatorPriceCourse(Booking booking)
    {
        TeeTime teeTime = teeTimeRepository.findById(booking.getTeeTimeId()).orElseThrow(
                () -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED)
        );
        return teeTime.getPrice() * booking.getNumPlayers();
    }

    public double calculatorPriceService(Booking booking) {
        List<BookingDetail> bookingDetails = bookingDetailRepository.findByBookingId(booking.getId());
        return bookingDetails.stream()
                .mapToDouble(BookingDetail::getTotalPrice)
                .sum();
    }


}

