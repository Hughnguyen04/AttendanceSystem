package com.attendance.repository;

import com.attendance.model.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Integer> {
    List<Absence> findByEmployeeIdOrderByCreatedAtDesc(Integer employeeId);
    List<Absence> findByEmployeeIdAndStatusOrderByStartDateDesc(Integer employeeId, String status);
    List<Absence> findByStatusAndStartDateBetween(String status, LocalDate startDate, LocalDate endDate);
}
