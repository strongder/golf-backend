package com.example.golf.service.impl;

import com.example.golf.dtos.ChatbotResponse;
import com.example.golf.dtos.tee_time.response.TeeTimeResponse;
import com.example.golf.model.GolfCourse;
import com.example.golf.model.MembershipType;
import com.example.golf.model.TeeTime;
import com.example.golf.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatBotServiceImpl {
    @Autowired
    ServicesRepository servicesRepository;
    @Autowired
    private GeneralInfoRepository generalInfoRepository;
    @Autowired
    private GolfCourseRepository golfCourseRepository;
    @Autowired
    private TeeTimeServiceImpl teeTimeServiceImpl;
    @Autowired
    private MemberShipRepository memberShipRepository;
    @Autowired
    private MemberShipTypeRepository memberShipTypeRepository;


    // hoi ve cac goi hoi vien
    public ChatbotResponse getMembershipPackages() {
        ChatbotResponse chatbotResponse = new ChatbotResponse();
        StringBuilder responseMessage = new StringBuilder("Các gói hội viên hiện có:\n");

        List<MembershipType> membershipTypes = memberShipTypeRepository.findAllByDeletedFalse();
        if (membershipTypes.isEmpty()) {
            responseMessage.append("Hiện tại không có gói hội viên nào.");
        } else {
            for (MembershipType membershipType : membershipTypes) {
                responseMessage.append("- ").append(membershipType.getName())
                        .append(" (Giá: ").append(membershipType.getPrice()).append(")\n");
            }
        }
        chatbotResponse.setMessage(responseMessage.toString());
        return chatbotResponse;
    }

    // hoi ve benefit cac goi hoi vien
    public ChatbotResponse getMembershipBenefits(String membershipName) {
        ChatbotResponse chatbotResponse = new ChatbotResponse();
        StringBuilder responseMessage = new StringBuilder("Lợi ích của gói hội viên " + membershipName + ":\n");

        MembershipType membershipType = memberShipTypeRepository.findByDeletedIsFalse(membershipName);
        if (membershipType == null) {
            responseMessage.append("Không tìm thấy gói hội viên với tên: ").append(membershipName);
        } else {
            responseMessage.append("- Giá: ").append(membershipType.getPrice()).append("\n")
                    .append("- Lợi ích: ").append(membershipType.getBenefits()).append("\n");
        }
        chatbotResponse.setMessage(responseMessage.toString());
        return chatbotResponse;
    }

    // hoi ve teeTime co san
    // tra loi cac khung thoi gian co san trong tung san

    public ChatbotResponse getAvailableTeeTimes(LocalDate date, LocalTime time) {
        List<GolfCourse> golfCourses = golfCourseRepository.findAll();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        if (time != null) {
            boolean availableAtTime = false;
            for (GolfCourse golfCourse : golfCourses) {
                List<TeeTimeResponse> availableTimes = teeTimeServiceImpl.getAvailableTeeTimes(golfCourse.getId(), date);

                boolean found = availableTimes.stream().anyMatch(teeTime -> {
                    // Giả sử teeTime.getStartTime() trả về String dạng "HH:mm" hoặc có thể parse được
                    LocalTime teeStartTime = LocalTime.parse(teeTime.getStartTime(), timeFormatter);
                    return teeStartTime.equals(time);
                });

                if (found) {
                    availableAtTime = true;
                    break;
                }
            }

            if (availableAtTime) {
                return ChatbotResponse.builder()
                        .message("Có, giờ chơi lúc " + time.format(timeFormatter) + " còn trống.")
                        .build();
            } else {
                return ChatbotResponse.builder()
                        .message("Không, giờ chơi lúc " + time.format(timeFormatter) + " đã hết chỗ.")
                        .build();
            }
        }

        // Nếu không có time (câu hỏi chung chung), trả về toàn bộ giờ trống
        StringBuilder responseMessage = new StringBuilder();
        for (GolfCourse golfCourse : golfCourses) {
            List<TeeTimeResponse> availableTimes = teeTimeServiceImpl.getAvailableTeeTimes(golfCourse.getId(), date);
            if (!availableTimes.isEmpty()) {
                responseMessage.append("Tee time có sẵn cho sân ")
                        .append(golfCourse.getName())
                        .append(" vào ngày ")
                        .append(date)
                        .append(":\n");
                availableTimes.forEach(teeTime ->
                        responseMessage.append("- ").append(teeTime.getStartTime()).append("\n")
                );
                responseMessage.append("\n");
            }
        }

        if (responseMessage.length() == 0) {
            return ChatbotResponse.builder()
                    .message("Rất tiếc, không có tee time trống vào ngày " + date + " cho bất kỳ sân nào.")
                    .build();
        }

        return ChatbotResponse.builder().message(responseMessage.toString()).build();
    }


    // hỏi ve dich vụ hiện có
    public ChatbotResponse getAvailableServices() {
        ChatbotResponse chatbotResponse = new ChatbotResponse();
        StringBuilder responseMessage = new StringBuilder("Hiện tai sân golf có các dịch vụ như:\n");

        servicesRepository.findAll().forEach(service -> {
            responseMessage.append("- ").append(service.getName()).append("\n");
        });

        chatbotResponse.setMessage(responseMessage.toString());
        return chatbotResponse;
    }

    // hỏi ve gia dich vụ
    public ChatbotResponse getPriceService(String serviceName) {
        System.out.println("Fetching price for service: " + serviceName);
        ChatbotResponse chatbotResponse = new ChatbotResponse();
        StringBuilder responseMessage = new StringBuilder("Giá của dịch vụ " + serviceName + " là:\n");

        String price = servicesRepository.getPriceByService(serviceName);
        if (price != null) {
            responseMessage.append(price).append("\n");
        } else {
            responseMessage.append("Không tìm thấy dịch vụ với tên: ").append(serviceName).append("\n");
        }

        chatbotResponse.setMessage(responseMessage.toString());
        return chatbotResponse;
    }

    // hỏi ve gio mo cua
    public ChatbotResponse getOpeningHours() {
        ChatbotResponse chatbotResponse = new ChatbotResponse();
        StringBuilder responseMessage = new StringBuilder("Sân golf mo cửa t:\n");

        generalInfoRepository.findAll().stream().findFirst()
                .ifPresent(generalInfo -> {
                    responseMessage.append(generalInfo.getOpenTime())
                            .append(" tới ")
                            .append(generalInfo.getCloseTime())
                            .append(" mỗi ngày.\n");
                });
        chatbotResponse.setMessage(responseMessage.toString());
        return chatbotResponse;
    }

    // hoi ve dia chi san
    public ChatbotResponse getAddress() {
        ChatbotResponse chatbotResponse = new ChatbotResponse();
        StringBuilder responseMessage = new StringBuilder("Địa chỉ sân golf là:\n");

        generalInfoRepository.findAll().stream().findFirst()
                .ifPresent(generalInfo -> {
                    responseMessage.append(generalInfo.getAddress()).append("\n");
                });
        chatbotResponse.setMessage(responseMessage.toString());
        return chatbotResponse;
    }

    // hoi ve thong tin lien he
    public ChatbotResponse getContactInfo() {
        ChatbotResponse chatbotResponse = new ChatbotResponse();
        StringBuilder responseMessage = new StringBuilder("Thông tin liên hệ:\n");

        generalInfoRepository.findAll().stream().findFirst()
                .ifPresent(generalInfo -> {
                    responseMessage.append("Điện thoại: ").append(generalInfo.getPhone()).append("\n")
                            .append("Email: ").append(generalInfo.getEmail()).append("\n");
                });
        chatbotResponse.setMessage(responseMessage.toString());
        return chatbotResponse;
    }
}
