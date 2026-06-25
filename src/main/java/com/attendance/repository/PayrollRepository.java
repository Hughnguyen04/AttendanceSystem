package com.attendance.repository;

import com.attendance.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Integer> {
    List<Payroll> findByEmployeeIdOrderByPeriodStartDesc(Integer employeeId);
    Optional<Payroll> findByEmployeeIdAndPeriodStartAndPeriodEnd(Integer employeeId, LocalDate periodStart, LocalDate periodEnd);
    List<Payroll> findByStatusOrderByCreatedAtDesc(String status);
}
