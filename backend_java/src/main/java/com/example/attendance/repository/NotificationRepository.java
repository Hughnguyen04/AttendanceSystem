package com.example.attendance.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByEmployeeIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            Long employeeId,
            LocalDateTime start,
            LocalDateTime end);

    List<Notification> findByIdInAndEmployeeId(List<Long> ids, Long employeeId);
}
