package com.example.attendance.repository;

import com.example.attendance.entity.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
    List<AttendanceLog> findByEmployeeIdOrderByCheckedTimeAsc(Long employeeId);
}
