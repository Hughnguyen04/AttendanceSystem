package com.attendance.repository;

import com.attendance.model.EmployeeBenefitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeBenefitLogRepository extends JpaRepository<EmployeeBenefitLog, Integer> {
    List<EmployeeBenefitLog> findByEmployeeIdOrderByCreatedAtDesc(Integer employeeId);
    List<EmployeeBenefitLog> findByEmployeeIdAndBenefitTypeOrderByIssueDateDesc(Integer employeeId, String benefitType);
    List<EmployeeBenefitLog> findByIssueDateBetweenOrderByCreatedAtDesc(LocalDate startDate, LocalDate endDate);
}
