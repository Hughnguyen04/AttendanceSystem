package com.example.attendance.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.attendance.repository.AbsencePlanRepository;
import com.example.attendance.repository.AbsenceRepository;
import com.example.attendance.repository.AttendanceCorrectionRequestRepository;
import com.example.attendance.repository.DailyWorkReportRepository;
import com.example.attendance.repository.EmployeeRepository;
import com.example.attendance.repository.MonthlyWorkReportRepository;
import com.example.attendance.repository.TimesheetPeriodControlRepository;

class PayrollServiceTest {
    @Test
    void calculateEstimatedMinutesUsesWorkingDaysCount() {
        MonthlyWorkReportRepository monthlyWorkReportRepository = mock(MonthlyWorkReportRepository.class);
        TimesheetPeriodControlRepository periodRepository = mock(TimesheetPeriodControlRepository.class);
        DailyWorkReportRepository dailyWorkReportRepository = mock(DailyWorkReportRepository.class);
        AbsenceRepository absenceRepository = mock(AbsenceRepository.class);
        AttendanceCorrectionRequestRepository correctionRepository = mock(AttendanceCorrectionRequestRepository.class);
        AbsencePlanRepository absencePlanRepository = mock(AbsencePlanRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        CalendarService calendarService = mock(CalendarService.class);

        PayrollService payrollService = new PayrollService(
                monthlyWorkReportRepository,
                periodRepository,
                dailyWorkReportRepository,
                absenceRepository,
                correctionRepository,
                absencePlanRepository,
                employeeRepository,
                calendarService
        );

        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 1, 3);
        when(calendarService.getWorkingDaysList(start, end)).thenReturn(List.of(start, start.plusDays(1), start.plusDays(2)));

        int minutes = payrollService.calculateEstimatedMinutes(start, end);

        assertEquals(1440, minutes);
    }
}
