package com.example.attendance.service;

import com.example.attendance.entity.AttendanceLog;
import com.example.attendance.entity.Employee;
import com.example.attendance.repository.AttendanceLogRepository;
import com.example.attendance.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceService {
    private final AttendanceLogRepository attendanceLogRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceService(AttendanceLogRepository attendanceLogRepository, EmployeeRepository employeeRepository) {
        this.attendanceLogRepository = attendanceLogRepository;
        this.employeeRepository = employeeRepository;
    }

    public AttendanceLog manualCheckIn(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên"));

        AttendanceLog log = new AttendanceLog();
        log.setEmployeeId(employee.getId());
        log.setLogDate(LocalDate.now());
        log.setShiftStart(LocalTime.of(8, 30));
        log.setShiftEnd(LocalTime.of(17, 30));
        log.setCheckedTime(LocalTime.now());
        return attendanceLogRepository.save(log);
    }

    public List<AttendanceLog> getLogsByEmployee(Long employeeId) {
        return attendanceLogRepository.findByEmployeeIdOrderByCheckedTimeAsc(employeeId);
    }
}
