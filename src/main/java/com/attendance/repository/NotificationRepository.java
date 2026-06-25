package com.attendance.repository;

import com.attendance.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByEmployeeIdOrderByCreatedAtDesc(Integer employeeId);
    List<Notification> findByEmployeeIdAndIsReadFalseOrderByCreatedAtDesc(Integer employeeId);
    List<Notification> findByEmployeeIdAndTypeOrderByCreatedAtDesc(Integer employeeId, String type);
    Integer countByEmployeeIdAndIsReadFalse(Integer employeeId);
}
