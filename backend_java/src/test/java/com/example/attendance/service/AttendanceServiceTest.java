package com.example.attendance.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.attendance.entity.AttendanceLog;
import com.example.attendance.entity.DailyWorkReport;
import com.example.attendance.repository.AttendanceLogRepository;
import com.example.attendance.repository.DailyWorkReportRepository;
import com.example.attendance.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceLogRepository attendanceLogRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DailyWorkReportRepository dailyWorkReportRepository;

    private AttendanceService attendanceService;

    @BeforeEach
    void setUp() {
        attendanceService = new AttendanceService(attendanceLogRepository, employeeRepository, dailyWorkReportRepository);
    }

    @Test
    void getDailyReportsByMonthShouldCreateMissingReportsFromAttendanceLogs() {
        Long employeeId = 1L;
        int month = 7;
        int year = 2026;
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        LocalDate workDate = LocalDate.of(2026, 7, 5);

        AttendanceLog log = new AttendanceLog();
        log.setEmployeeId(employeeId);
        log.setLogDate(workDate);
        log.setShiftStart(LocalTime.of(8, 30));
        log.setShiftEnd(LocalTime.of(17, 30));
        log.setCheckedTime(LocalTime.of(8, 0));

        DailyWorkReport savedReport = new DailyWorkReport();
        when(attendanceLogRepository.findByEmployeeIdAndLogDateBetweenOrderByLogDateAscCheckedTimeAsc(employeeId, startDate, endDate))
                .thenReturn(List.of(log));
        when(dailyWorkReportRepository.findByEmployeeIdAndWorkDateBetween(employeeId, startDate, endDate))
                .thenReturn(List.of(savedReport));
        when(attendanceLogRepository.findByEmployeeIdAndLogDateOrderByCheckedTimeAsc(employeeId, workDate))
                .thenReturn(List.of(log));
        when(dailyWorkReportRepository.findByEmployeeIdAndWorkDate(employeeId, workDate))
                .thenReturn(Optional.empty());
        savedReport.setEmployeeId(employeeId);
        savedReport.setWorkDate(workDate);
        savedReport.setCheckIn(LocalTime.of(8, 0));
        savedReport.setCheckOut(LocalTime.of(8, 0));
        savedReport.setWorkTimeMinutes(0);
        when(dailyWorkReportRepository.save(any(DailyWorkReport.class))).thenReturn(savedReport);

        List<DailyWorkReport> reports = attendanceService.getDailyReportsByMonth(employeeId, month, year);

        assertEquals(1, reports.size());
        verify(dailyWorkReportRepository).save(any(DailyWorkReport.class));
    }
}
