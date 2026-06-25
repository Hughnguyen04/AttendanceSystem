package com.attendance.repository;

import com.attendance.model.DailyWorkReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyWorkReportRepository extends JpaRepository<DailyWorkReport, Integer> {
    List<DailyWorkReport> findByEmployeeIdOrderByWorkDateDesc(Integer employeeId);
    Optional<DailyWorkReport> findByEmployeeIdAndWorkDate(Integer employeeId, LocalDate workDate);
    List<DailyWorkReport> findByEmployeeIdAndWorkDateBetweenOrderByWorkDateDesc(Integer employeeId, LocalDate startDate, LocalDate endDate);
}
