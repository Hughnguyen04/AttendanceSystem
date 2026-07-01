package com.example.attendance.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.attendance.entity.AttendanceLog;
import com.example.attendance.entity.DailyWorkReport;
import com.example.attendance.entity.Employee;
import com.example.attendance.repository.AttendanceLogRepository;
import com.example.attendance.repository.DailyWorkReportRepository;
import com.example.attendance.repository.EmployeeRepository;

@Service
public class AttendanceService {
    private final AttendanceLogRepository attendanceLogRepository;
    private final EmployeeRepository employeeRepository;
    private final DailyWorkReportRepository dailyWorkReportRepository;

    public AttendanceService(AttendanceLogRepository attendanceLogRepository,
                              EmployeeRepository employeeRepository,
                              DailyWorkReportRepository dailyWorkReportRepository) {
        this.attendanceLogRepository = attendanceLogRepository;
        this.employeeRepository = employeeRepository;
        this.dailyWorkReportRepository = dailyWorkReportRepository;
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

    public List<DailyWorkReport> getDailyReportsByMonth(Long employeeId, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return dailyWorkReportRepository.findByEmployeeIdAndWorkDateBetween(employeeId, startDate, endDate);
    }
}
