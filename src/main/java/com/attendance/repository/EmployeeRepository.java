package com.attendance.repository;

import com.attendance.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByIdAndIsActiveTrue(Integer id);
    List<Employee> findByIsActiveTrueOrderByIdDesc();
}
