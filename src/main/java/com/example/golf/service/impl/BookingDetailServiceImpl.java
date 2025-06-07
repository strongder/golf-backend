package com.example.golf.service.impl;

import com.example.golf.dtos.booking_detail.request.BookingDetailRequest;
import com.example.golf.dtos.booking_detail.response.BookingDetailResponse;
import com.example.golf.dtos.services.response.ServicesResponse;
import com.example.golf.dtos.tool.response.ToolResponse;
import com.example.golf.enums.ErrorResponse;
import com.example.golf.exception.AppException;
import com.example.golf.model.*;
import com.example.golf.repository.*;
import com.example.golf.service.BookingDetailService;
import com.example.golf.service.ToolService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@org.springframework.stereotype.Service
public class BookingDetailServiceImpl extends BaseServiceImpl<BookingDetail, String> implements BookingDetailService {

    @Autowired
    private final BookingDetailRepository bookingDetailsRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ServicesRepository serviceRepository;
    @Autowired
    private ToolsRepository toolsRepository;
    @Autowired
    private ToolService toolsService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TeeTimeRepository teeTimeRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    public BookingDetailServiceImpl(BookingDetailRepository bookingDetailsRepository) {
        super(bookingDetailsRepository);
        this.bookingDetailsRepository = bookingDetailsRepository;
    }

    @Transactional
    public void createBookingDetail(String bookingId, List<BookingDetailRequest> request) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        double totalPrice = 0.0;
        for (BookingDetailRequest detail : request) {
            BookingDetail bookingDetail = modelMapper.map(detail, BookingDetail.class);
            bookingDetail.setBookingId(bookingId);
            if ((bookingDetail.getToolId() != null) && !bookingDetail.getToolId().isEmpty()) {
                Tool tool = toolsRepository.findById(bookingDetail.getToolId()).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
                toolsService.calcQuantity(bookingDetail.getToolId(), bookingDetail.getQuantity(), true);
                totalPrice += tool.getRentPrice() * bookingDetail.getQuantity();
                bookingDetail.setUnitPrice(tool.getRentPrice());
                bookingDetail.setTotalPrice(tool.getRentPrice() * bookingDetail.getQuantity());
            } else {
                Services service = serviceRepository.findById(bookingDetail.getServiceId()).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
                totalPrice += service.getPrice() * bookingDetail.getQuantity();
                bookingDetail.setUnitPrice(service.getPrice());
                bookingDetail.setTotalPrice(service.getPrice() * bookingDetail.getQuantity());
            }
            this.save(bookingDetail);
        }
        booking.setTotalCost(booking.getTotalCost() + totalPrice);
        bookingRepository.save(booking);
    }

    //    viet ham update booking detail
    @Transactional
    public void updateBookingDetail(String bookingId, List<BookingDetailRequest> requestList) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));

        Map<String, BookingDetail> existingMap = bookingDetailRepository.findByBookingId(bookingId)
                .stream().collect(Collectors.toMap(BookingDetail::getId, bd -> bd));

        Set<String> requestIds = requestList.stream()
                .map(BookingDetailRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Xóa những cái không còn trong request
        for (BookingDetail old : existingMap.values()) {
            if (!requestIds.contains(old.getId())) {
//                toolsService.calcQuantity(old.getToolId(), old.getQuantity(), false);
                bookingDetailRepository.delete(old);
            }
        }
        double totalServiceCost = 0.0;
        for (BookingDetailRequest detail : requestList) {
            BookingDetail bd;
            if (detail.getId() != null && existingMap.containsKey(detail.getId())) {
                bd = existingMap.get(detail.getId());
            } else {
                bd = new BookingDetail();
                bd.setBookingId(bookingId);
            }
            bd.setQuantity(detail.getQuantity());
            bd.setServiceId(detail.getServiceId());
            bd.setToolId(detail.getToolId());

            double price = 0.0;
            if (detail.getToolId() != null) {
                Tool tool = toolsRepository.findById(detail.getToolId())
                        .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
                price = tool.getRentPrice();
//                toolsService.calcQuantity(detail.getToolId(), detail.getQuantity(), true);
            } else {
                Services service = serviceRepository.findById(detail.getServiceId())
                        .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
                price = service.getPrice();
            }

            bd.setTotalPrice(price * detail.getQuantity());
            totalServiceCost += bd.getTotalPrice();
            bookingDetailRepository.save(bd);
        }

        // Cập nhật lại tổng chi phí
        TeeTime teeTime = teeTimeRepository.findById(booking.getTeeTimeId())
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        double teeTimeCost = teeTime.getPrice() * booking.getNumPlayers();

        booking.setTotalCost(teeTimeCost + totalServiceCost);
        bookingRepository.save(booking);
    }


    @Transactional(readOnly = true)
    public List<BookingDetailResponse> getDetailByBooking(String bookingId) {
        List<BookingDetail> bookingDetails = bookingDetailsRepository.findByBookingId(bookingId);
        return bookingDetails.stream().map(bookingDetail -> convertToResponse(bookingDetail, BookingDetailResponse.class)).toList();
    }

    @Transactional
    public String softDelete(String id) {
        BookingDetail bookingDetail = bookingDetailsRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        bookingDetail.setDeleted(true);
        bookingDetailsRepository.save(bookingDetail);
        return id;
    }

    @Override
    protected <Res> Res convertToResponse(BookingDetail entity, Class<Res> responseType) {
        BookingDetailResponse response = modelMapper.map(entity, BookingDetailResponse.class);

        Services service = serviceRepository.findById(entity.getServiceId())
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        response.setService(modelMapper.map(service, ServicesResponse.class));

        if (entity.getToolId() != null) {
            Tool tool = toolsRepository.findById(entity.getToolId())
                    .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
            response.setTool(modelMapper.map(tool, ToolResponse.class));
        }
        return responseType.cast(response);
    }




    @Override
    protected <Req> BookingDetail convertToEntity(Req request) {
        return null;
    }
}
