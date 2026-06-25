package com.attendance.repository;

import com.attendance.model.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Integer> {
    List<AttendanceLog> findByEmployeeIdAndWorkDate(Integer employeeId, LocalDate workDate);
    List<AttendanceLog> findByEmployeeIdOrderByWorkDateDesc(Integer employeeId);
    Optional<AttendanceLog> findByEmployeeIdAndWorkDateOrderByCheckInTimeDesc(Integer employeeId, LocalDate workDate);
}
