package com.attendance.repository;

import com.attendance.model.TimesheetPeriodControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimesheetPeriodControlRepository extends JpaRepository<TimesheetPeriodControl, Integer> {
    Optional<TimesheetPeriodControl> findByPeriodStartAndPeriodEnd(LocalDate periodStart, LocalDate periodEnd);
    List<TimesheetPeriodControl> findByStatusOrderByPeriodStartDesc(String status);
    Optional<TimesheetPeriodControl> findByPeriodNameAndStatus(String periodName, String status);
}
