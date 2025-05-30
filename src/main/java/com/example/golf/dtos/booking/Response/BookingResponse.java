package com.example.golf.dtos.booking.Response;

import com.example.golf.dtos.DataField;
import com.example.golf.dtos.golf_course.response.DataFieldGolfCourse;
import com.example.golf.dtos.tee_time.response.DataFieldTeeTime;
import com.example.golf.dtos.tee_time.response.TeeTimeResponse;
import com.example.golf.enums.BookingStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Data
public class BookingResponse {
    private String id; // Mã booking
    private String bookingCode; // Mã đặt sân
    private String phone; // Số điện thoại khách đặt sân
    private String fullName; // Tên khách đặt sân
    private String email; // Email khách đặt sân
    private String golferId; // NULL nếu là khách vãng lai, hoặc ID golfer nếu có tài khoản
    private DataFieldGolfCourse golfCourse; // Sân golf được đặt
    private LocalDate bookingDate; // Ngày đặt sân
    private DataFieldTeeTime teeTime; // Thời gian đặt sân
    private int numPlayers; // Số người chơi đặt trước
    private int numberOfHoles; // Số lỗ golf được đặt
    private BookingStatus status; // PENDING, CONFIRMED, PLAYING, COMPLETED, CHECKED_OUT
    private LocalDateTime checkInTime; // Thời gian check-in thực tế
    private LocalDateTime checkOutTime; // Thời gian check-out thực tế
    private double depositAmount; // Số tiền khách đã đặt cọc
    private String checkInBy; // Nhân viên thực hiện check-in
    private double priceByCourse;
    private double priceByService;
    private double totalCost; // Tổng chi phí (sân golf + dịch vụ)
    private String checkOutBy; // Nhân viên thực hiện check-out
    private String note; // Ghi chú của khách hàng
    private String paymentMethod; // "cash", , "VNPay"
    private String createdBy; // Người tạo booking
    private String updatedBy; // Người cập nhật booking
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}