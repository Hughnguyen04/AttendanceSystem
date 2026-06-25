package com.attendance.repository;

import com.attendance.model.OvertimeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OvertimeRequestRepository extends JpaRepository<OvertimeRequest, Integer> {
    List<OvertimeRequest> findByEmployeeIdOrderByCreatedAtDesc(Integer employeeId);
    List<OvertimeRequest> findByEmployeeIdAndStatusOrderByWorkDateDesc(Integer employeeId, String status);
    List<OvertimeRequest> findByStatusAndWorkDateBetween(String status, LocalDate startDate, LocalDate endDate);
}
