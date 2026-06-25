package com.attendance.repository;

import com.attendance.model.ShiftChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShiftChangeRequestRepository extends JpaRepository<ShiftChangeRequest, Integer> {
    List<ShiftChangeRequest> findByEmployeeIdOrderByCreatedAtDesc(Integer employeeId);
    List<ShiftChangeRequest> findByEmployeeIdAndStatusOrderByEffectiveDateDesc(Integer employeeId, String status);
    List<ShiftChangeRequest> findByStatusOrderByCreatedAtDesc(String status);
}
