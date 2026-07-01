package com.example.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.AttendanceLog;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
    List<AttendanceLog> findByEmployeeIdOrderByCheckedTimeAsc(Long employeeId);
    List<AttendanceLog> findByEmployeeIdAndLogDateOrderByCheckedTimeAsc(Long employeeId, LocalDate logDate);
}
