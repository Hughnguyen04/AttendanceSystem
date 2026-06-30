package com.example.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.WorkCompensation;

@Repository
public interface WorkCompensationRepository extends JpaRepository<WorkCompensation, Long> {
    boolean existsByCompensateDate(LocalDate compensateDate);
    boolean existsByCompensateDateAndIdNot(LocalDate compensateDate, Long id);
    List<WorkCompensation> findByCompensateDateBetween(LocalDate startDate, LocalDate endDate);
}

