package com.example.attendance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.AbsenceTracker;

@Repository
public interface AbsenceTrackerRepository extends JpaRepository<AbsenceTracker, Long> {
    Optional<AbsenceTracker> findByEmployeeId(Long employeeId);
}
