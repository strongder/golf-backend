package com.example.golf.service.impl;

import com.example.golf.model.Notification;
import com.example.golf.model.User;
import com.example.golf.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class NotificationServiceImpl {

    // Lưu trữ danh sách emitter cho mỗi userId
    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    // Thời gian timeout của mỗi kết nối SSE (có thể để rất lớn để giữ kết nối lâu)
    private static final Long TIMEOUT = 60 * 60 * 1000L; // 1 giờ
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;
    /**
     * Đăng ký subscribe SSE cho userId.
     * Mỗi kết nối sẽ tạo một SseEmitter mới, thêm vào danh sách emitter của user.
     */
    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        // Thêm emitter vào danh sách của user
        emitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);
        // Xử lý sự kiện kết nối hoàn thành, timeout hoặc lỗi: remove emitter khỏi danh sách
        emitter.onCompletion(() -> removeEmitter(userId, emitter));
        emitter.onTimeout(() -> removeEmitter(userId, emitter));
        emitter.onError((e) -> removeEmitter(userId, emitter));

        // Optional: gửi sự kiện keep-alive để giữ kết nối mở
        try {
            emitter.send(SseEmitter.event().comment("keep-alive"));
        } catch (IOException e) {
            // Nếu gửi không được thì đóng emitter
            emitter.complete();
            removeEmitter(userId, emitter);
        }

        return emitter;
    }

    /**
     * Xóa emitter khỏi danh sách của user khi kết nối bị đóng, lỗi hoặc timeout.
     */
    private void removeEmitter(String userId, SseEmitter emitter) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(emitter);
            if (userEmitters.isEmpty()) {
                emitters.remove(userId);
            }
        }
    }

    /**
     * Gửi notification đến tất cả kết nối SSE đang mở của user.
     */
    public void sendNotification(Notification data) {
        List<SseEmitter> userEmitters = emitters.get(data.getUserId());
        if (userEmitters != null) {
            for (SseEmitter emitter : userEmitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .data(data));
                } catch (IOException e) {
                    emitter.complete();
                    removeEmitter(data.getUserId(), emitter);
                }
            }
        }
    }

    @Transactional
    public List<Notification> getByUser() {
        User user = userServiceImpl.getCurrentUser();
        return notificationRepository.findByUserId(user.getId());
    }

    @Transactional
    public String markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
        return notificationId;
    }

    @Transactional
    public void markAllAsRead(String userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public String deleteNotification(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
        return notificationId;
    }
}
