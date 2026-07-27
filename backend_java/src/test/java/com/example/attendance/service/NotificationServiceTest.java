package com.example.attendance.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.attendance.dto.NotificationResponse;
import com.example.attendance.entity.Notification;
import com.example.attendance.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void getNotificationsForEmployee_returnsMappedNotifications() {
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setEmployeeId(10L);
        notification.setTitle("Chúc mừng");
        notification.setContent("Bạn có thông báo mới");
        notification.setIsRead(false);
        notification.setNotificationType("SYSTEM");
        notification.setCreatedAt(LocalDateTime.of(2026, 7, 27, 10, 0));

        LocalDateTime start = YearMonth.of(2026, 7).atDay(1).atStartOfDay();
        LocalDateTime end = YearMonth.of(2026, 7).atEndOfMonth().atTime(23, 59, 59, 999_999_999);

        when(notificationRepository.findByEmployeeIdAndCreatedAtBetweenOrderByCreatedAtDesc(10L, start, end))
                .thenReturn(List.of(notification));

        List<NotificationResponse> result = notificationService.getNotificationsForEmployee(10L, 7, 2026);

        assertEquals(1, result.size());
        assertEquals("Chúc mừng", result.get(0).getTitle());
        assertEquals("Bạn có thông báo mới", result.get(0).getContent());
        assertTrue(result.get(0).getIsRead() == null || !result.get(0).getIsRead());
    }

    @Test
    void markAsRead_updatesMatchingNotifications() {
        Notification notification = new Notification();
        notification.setId(2L);
        notification.setEmployeeId(11L);
        notification.setIsRead(false);

        when(notificationRepository.findByIdInAndEmployeeId(List.of(2L), 11L)).thenReturn(List.of(notification));

        int updatedCount = notificationService.markAsRead(11L, List.of(2L));

        assertEquals(1, updatedCount);
        assertTrue(notification.getIsRead());
        verify(notificationRepository).saveAll(List.of(notification));
    }
}
