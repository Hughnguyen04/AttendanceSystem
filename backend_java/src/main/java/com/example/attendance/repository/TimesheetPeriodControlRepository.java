package com.example.attendance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.TimesheetPeriodControl;

@Repository
public interface TimesheetPeriodControlRepository extends JpaRepository<TimesheetPeriodControl, Long> {
    Optional<TimesheetPeriodControl> findByMonthAndYear(Integer month, Integer year);
}
