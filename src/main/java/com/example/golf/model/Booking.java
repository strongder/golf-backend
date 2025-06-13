package com.example.golf.model;
import com.example.golf.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "booking")
public class Booking extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id; // Mã booking
    private String bookingCode; // Mã đặt sân
    private String phone; // Số điện thoại khách đặt sân
    private String fullName; // Tên khách đặt sân
    private String email; // Email khách đặt sân
    private String golferId; // id khach hàng
    private String userId;   // id nhan vien hoac nguoi dung co tai khoan
    private String golfCourseId; // Sân golf được đặt
    private String teeTimeId; // Thời gian tee time được đặt
    private LocalDate bookingDate; // Ngày đặt sân
    private int numPlayers; // Số người chơi đặt trước
    private int numberOfHoles; // Số lỗ golf được đặt
    @Enumerated(EnumType.STRING)
    private BookingStatus status; // PENDING ,CONFIRMED, PLAYING, COMPLETED
    private LocalDateTime checkInTime; // Thời gian check-in thực tế
    private LocalDateTime checkOutTime; // Thời gian check-out thực tế
    private double depositAmount; // Số tiền khách đã đặt cọc
    private double totalCost; // Tổng chi phí (sân golf + dịch vụ)
    private String checkInBy;
    private String checkOutBy; // Nhân viên thực hiện check-out
    private String paymentMethod; //"cash", , "VNPay"
    private String discountPromotion; // Mã giảm giá nếu có
    private String discountMembership; // Mã giảm giá thành viên nếu có
    private String note;;

}
