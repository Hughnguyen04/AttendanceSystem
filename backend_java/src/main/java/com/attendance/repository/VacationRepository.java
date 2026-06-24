package com.attendance.repository;

import com.attendance.model.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Integer> {
    List<Vacation> findByEmployeeIdOrderByCreatedAtDesc(Integer employeeId);
    List<Vacation> findByEmployeeIdAndStatusOrderByStartDateDesc(Integer employeeId, String status);
    List<Vacation> findByStatusAndStartDateBetween(String status, LocalDate startDate, LocalDate endDate);
}
