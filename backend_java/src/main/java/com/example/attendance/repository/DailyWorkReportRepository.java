package com.example.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.DailyWorkReport;

@Repository
public interface DailyWorkReportRepository extends JpaRepository<DailyWorkReport, Long> {
    List<DailyWorkReport> findByEmployeeIdAndWorkDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}
