package com.attendance.repository;

import com.attendance.model.AttendanceCorrection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceCorrectionRepository extends JpaRepository<AttendanceCorrection, Integer> {
    List<AttendanceCorrection> findByEmployeeIdOrderByCreatedAtDesc(Integer employeeId);
    List<AttendanceCorrection> findByEmployeeIdAndStatusOrderByWorkDateDesc(Integer employeeId, String status);
    List<AttendanceCorrection> findByStatusOrderByCreatedAtDesc(String status);
    List<AttendanceCorrection> findByWorkDateBetweenOrderByCreatedAtDesc(LocalDate startDate, LocalDate endDate);
}
