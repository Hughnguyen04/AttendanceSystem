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

        AttendanceLog savedLog = attendanceLogRepository.save(log);

        try {
            calculateDailyWorkReport(employeeId, savedLog.getLogDate());
        } catch (Exception ignored) {
            // Keep check-in successful even if report calculation fails once.
        }

        return savedLog;
    }

    public List<AttendanceLog> getLogsByEmployee(Long employeeId) {
        return attendanceLogRepository.findByEmployeeIdOrderByCheckedTimeAsc(employeeId);
    }

    public List<DailyWorkReport> getDailyReportsByMonth(Long employeeId, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return dailyWorkReportRepository.findByEmployeeIdAndWorkDateBetween(employeeId, startDate, endDate);
    }

    public DailyWorkReport calculateDailyWorkReport(Long employeeId, LocalDate workDate) {
        List<AttendanceLog> logs = attendanceLogRepository.findByEmployeeIdAndLogDateOrderByCheckedTimeAsc(employeeId, workDate);
        if (logs.isEmpty()) {
            throw new IllegalArgumentException("Không có dữ liệu chấm công cho ngày này");
        }

        AttendanceLog firstLog = logs.get(0);
        AttendanceLog lastLog = logs.get(logs.size() - 1);

        DailyWorkReport report = dailyWorkReportRepository.findByEmployeeIdAndWorkDate(employeeId, workDate)
                .orElseGet(DailyWorkReport::new);

        int checkInMinutes = toMinutes(firstLog.getCheckedTime());
        int checkOutMinutes = toMinutes(lastLog.getCheckedTime());
        int shiftStartMinutes = toMinutes(firstLog.getShiftStart());
        int shiftEndMinutes = toMinutes(firstLog.getShiftEnd());

        int inOfficeMinutes = Math.max(0, checkOutMinutes - checkInMinutes);
        int lateMinutes = Math.max(0, checkInMinutes - shiftStartMinutes);
        int earlyMinutes = Math.max(0, shiftEndMinutes - checkOutMinutes);

        report.setEmployeeId(employeeId);
        report.setWorkDate(workDate);
        report.setCheckIn(firstLog.getCheckedTime());
        report.setCheckOut(lastLog.getCheckedTime());
        report.setInOfficeMinutes(inOfficeMinutes);
        report.setWorkTimeMinutes(inOfficeMinutes);
        report.setLateArriveMinutes(lateMinutes);
        report.setLeaveEarlyMinutes(earlyMinutes);
        report.setLackMinutes(0);
        report.setOvertimeMinutes(0);

        return dailyWorkReportRepository.save(report);
    }

    private int toMinutes(LocalTime time) {
        if (time == null) {
            return 0;
        }
        return time.getHour() * 60 + time.getMinute();
    }
}
