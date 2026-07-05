package com.example.attendance.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.MonthlyWorkReport;

@Repository
public interface MonthlyWorkReportRepository extends JpaRepository<MonthlyWorkReport, Long> {
    Optional<MonthlyWorkReport> findByEmployeeIdAndPeriodStart(Long employeeId, LocalDate periodStart);
    void deleteByPeriodStart(LocalDate periodStart);
}
