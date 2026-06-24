package com.attendance.repository;

import com.attendance.model.MonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyReportRepository extends JpaRepository<MonthlyReport, Integer> {
    List<MonthlyReport> findByEmployeeIdOrderByYearDescMonthDesc(Integer employeeId);
    Optional<MonthlyReport> findByEmployeeIdAndMonthAndYear(Integer employeeId, Integer month, Integer year);
    List<MonthlyReport> findByYearAndMonthOrderByEmployeeIdAsc(Integer year, Integer month);
}
