package com.example.attendance.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.ApprovalStatus;
import com.example.attendance.entity.AttendanceCorrectionRequest;

@Repository
public interface AttendanceCorrectionRequestRepository extends JpaRepository<AttendanceCorrectionRequest, Long> {
    java.util.List<AttendanceCorrectionRequest> findByEmployeeId(Long employeeId);

    java.util.List<AttendanceCorrectionRequest> findByEmployeeIdAndWorkDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM AttendanceCorrectionRequest a, Employee e " +
           "WHERE a.employeeId = e.id " +
           "AND (:month IS NULL OR MONTH(a.workDate) = :month) " +
           "AND (:year IS NULL OR YEAR(a.workDate) = :year) " +
           "AND (:status IS NULL OR a.status = :status) " +
           "AND (:search IS NULL OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :search, '%')))" )
    Page<AttendanceCorrectionRequest> findByFilters(@Param("month") Integer month,
                                                    @Param("year") Integer year,
                                                    @Param("status") ApprovalStatus status,
                                                    @Param("search") String search,
                                                    Pageable pageable);
}

