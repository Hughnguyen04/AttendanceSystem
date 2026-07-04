package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.AbsencePlan;
import com.example.attendance.entity.ApprovalStatus;

@Repository
public interface AbsencePlanRepository extends JpaRepository<AbsencePlan, Long> {
    List<AbsencePlan> findByEmployeeIdOrderByStartDateDesc(Long employeeId);

    @Query("SELECT a FROM AbsencePlan a JOIN Employee e ON a.employeeId = e.id " +
           "WHERE (:status IS NULL OR a.status = :status) " +
           "AND (:search IS NULL OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY a.createdAt DESC")
    Page<AbsencePlan> findAdminPlans(@Param("status") ApprovalStatus status,
                                     @Param("search") String search,
                                     Pageable pageable);

    boolean existsByEmployeeIdAndStatusNotAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long employeeId,
            ApprovalStatus status,
            java.time.LocalDate endDate,
            java.time.LocalDate startDate);
}
