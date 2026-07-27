package com.example.attendance.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.attendance.dto.DashboardStatisticResponse;
import com.example.attendance.entity.Absence;
import com.example.attendance.entity.AbsenceType;
import com.example.attendance.entity.DailyWorkReport;
import com.example.attendance.entity.Employee;
import com.example.attendance.repository.AbsenceRepository;
import com.example.attendance.repository.DailyWorkReportRepository;
import com.example.attendance.repository.EmployeeRepository;

@Service
public class StatisticService {
    private final EmployeeRepository employeeRepository;
    private final DailyWorkReportRepository dailyWorkReportRepository;
    private final AbsenceRepository absenceRepository;

    public StatisticService(EmployeeRepository employeeRepository,
            DailyWorkReportRepository dailyWorkReportRepository,
            AbsenceRepository absenceRepository) {
        this.employeeRepository = employeeRepository;
        this.dailyWorkReportRepository = dailyWorkReportRepository;
        this.absenceRepository = absenceRepository;
    }

    public DashboardStatisticResponse getDashboardStats(int monthsBack) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays((long) monthsBack * 30L);

        Set<AbsenceType> excludedTypes = new HashSet<>();
        excludedTypes.add(AbsenceType.MATERNITY);
        excludedTypes.add(AbsenceType.WEDDING);
        excludedTypes.add(AbsenceType.FUNERAL);
        excludedTypes.add(AbsenceType.PATERNITY);

        List<Employee> employees = employeeRepository.findAll();
        List<DailyWorkReport> reports = dailyWorkReportRepository.findAll();
        List<Absence> absences = absenceRepository.findAll();

        List<DashboardStatisticResponse.TopHardWorking> topHardWorking = employees.stream()
                .map(employee -> {
                    int totalOffence = reports.stream()
                            .filter(report -> report.getEmployeeId() != null
                                    && report.getEmployeeId().equals(employee.getId())
                                    && !report.getWorkDate().isBefore(startDate)
                                    && !report.getWorkDate().isAfter(endDate))
                            .mapToInt(report -> (report.getLateArriveMinutes() == null ? 0 : report.getLateArriveMinutes())
                                    + (report.getLeaveEarlyMinutes() == null ? 0 : report.getLeaveEarlyMinutes()))
                            .sum();

                    int totalAbsences = (int) absences.stream()
                            .filter(absence -> absence.getEmployeeId() != null
                                    && absence.getEmployeeId().equals(employee.getId())
                                    && !absence.getWorkDate().isBefore(startDate)
                                    && !absence.getWorkDate().isAfter(endDate)
                                    && absence.getAbsenceType() != null
                                    && !excludedTypes.contains(absence.getAbsenceType()))
                            .count();

                    return new DashboardStatisticResponse.TopHardWorking(employee.getId(), employee.getFullName(),
                            totalOffence, totalAbsences);
                })
                .sorted(Comparator.comparingInt(DashboardStatisticResponse.TopHardWorking::getTotalAbsences)
                        .thenComparingInt(DashboardStatisticResponse.TopHardWorking::getTotalOffenceMinutes))
                .limit(10)
                .collect(Collectors.toList());

        List<DashboardStatisticResponse.TopLateLeaver> topLateLeavers = reports.stream()
                .filter(report -> report.getWorkDate() != null && !report.getWorkDate().isBefore(startDate)
                        && !report.getWorkDate().isAfter(endDate))
                .sorted(Comparator.comparing(DailyWorkReport::getCheckOut, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(10)
                .map(report -> new DashboardStatisticResponse.TopLateLeaver(report.getEmployeeId(),
                        findEmployeeName(report.getEmployeeId(), employees), report.getCheckOut(), report.getWorkDate()))
                .collect(Collectors.toList());

        long totalDays = reports.stream()
                .filter(report -> report.getWorkDate() != null && !report.getWorkDate().isBefore(startDate)
                        && !report.getWorkDate().isAfter(endDate))
                .count();

        long onTimeDays = reports.stream()
                .filter(report -> report.getWorkDate() != null && !report.getWorkDate().isBefore(startDate)
                        && !report.getWorkDate().isAfter(endDate)
                        && (report.getLateArriveMinutes() == null || report.getLateArriveMinutes() == 0)
                        && (report.getLeaveEarlyMinutes() == null || report.getLeaveEarlyMinutes() == 0))
                .count();

        double onTimePercentage = 0.0;
        double lateEarlyPercentage = 0.0;
        if (totalDays > 0) {
            onTimePercentage = Math.round((onTimeDays * 100.0 / totalDays) * 100.0) / 100.0;
            lateEarlyPercentage = Math.round((100.0 - onTimePercentage) * 100.0) / 100.0;
        }

        DashboardStatisticResponse.Ratios ratios = new DashboardStatisticResponse.Ratios(onTimePercentage,
                lateEarlyPercentage, 0.0);

        return new DashboardStatisticResponse(topHardWorking, topLateLeavers, ratios);
    }

    private String findEmployeeName(Long employeeId, List<Employee> employees) {
        if (employeeId == null) {
            return null;
        }
        return employees.stream().filter(employee -> employee.getId().equals(employeeId)).findFirst()
                .map(Employee::getFullName).orElse(null);
    }
}
