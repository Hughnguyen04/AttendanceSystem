package com.example.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.ApprovalStatus;
import com.example.attendance.entity.OvertimeRequest;

@Repository
public interface OvertimeRequestRepository extends JpaRepository<OvertimeRequest, Long> {
    List<OvertimeRequest> findByEmployeeId(Long employeeId);

    List<OvertimeRequest> findByEmployeeIdAndWorkDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    List<OvertimeRequest> findByEmployeeIdAndWorkDate(Long employeeId, LocalDate workDate);

    @Query("SELECT o FROM OvertimeRequest o " +
           "WHERE (:month IS NULL OR MONTH(o.workDate) = :month) " +
           "AND (:year IS NULL OR YEAR(o.workDate) = :year) " +
           "AND (:status IS NULL OR o.status = :status)")
    Page<OvertimeRequest> findByFilters(@Param("month") Integer month,
                                       @Param("year") Integer year,
                                       @Param("status") ApprovalStatus status,
                                       Pageable pageable);
}

