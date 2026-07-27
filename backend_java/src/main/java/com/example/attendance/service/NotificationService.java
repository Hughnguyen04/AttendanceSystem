package com.example.attendance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.attendance.dto.NotificationResponse;
import com.example.attendance.entity.Notification;
import com.example.attendance.repository.NotificationRepository;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationResponse> getNotificationsForEmployee(Long employeeId, Integer month, Integer year) {
        int targetMonth = month != null ? month : LocalDate.now().getMonthValue();
        int targetYear = year != null ? year : LocalDate.now().getYear();

        YearMonth yearMonth = YearMonth.of(targetYear, targetMonth);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999_999_999);

        return notificationRepository
                .findByEmployeeIdAndCreatedAtBetweenOrderByCreatedAtDesc(employeeId, start, end)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public int markAsRead(Long employeeId, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        List<Notification> notifications = notificationRepository.findByIdInAndEmployeeId(ids, employeeId);
        for (Notification notification : notifications) {
            notification.setIsRead(true);
        }
        notificationRepository.saveAll(notifications);
        return notifications.size();
    }

    private NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setTitle(notification.getTitle());
        response.setContent(notification.getContent());
        response.setIsRead(notification.getIsRead());
        response.setCreatedAt(notification.getCreatedAt());
        response.setNotificationType(notification.getNotificationType());
        return response;
    }
}
