package com.example.attendance.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.attendance.dto.MonthLyWorkReportResponse;
import com.example.attendance.entity.Absence;
import com.example.attendance.entity.AbsenceType;
import com.example.attendance.entity.ApprovalStatus;
import com.example.attendance.entity.AttendanceCorrectionRequest;
import com.example.attendance.entity.DailyWorkReport;
import com.example.attendance.entity.Employee;
import com.example.attendance.entity.MonthlyWorkReport;
import com.example.attendance.entity.TimesheetPeriodControl;
import com.example.attendance.repository.AbsencePlanRepository;
import com.example.attendance.repository.AbsenceRepository;
import com.example.attendance.repository.AttendanceCorrectionRequestRepository;
import com.example.attendance.repository.DailyWorkReportRepository;
import com.example.attendance.repository.EmployeeRepository;
import com.example.attendance.repository.MonthlyWorkReportRepository;
import com.example.attendance.repository.TimesheetPeriodControlRepository;

@Service
public class PayrollService {
    private final MonthlyWorkReportRepository monthlyWorkReportRepository;
    private final TimesheetPeriodControlRepository periodRepository;
    private final DailyWorkReportRepository dailyWorkReportRepository;
    private final AbsenceRepository absenceRepository;
    private final AttendanceCorrectionRequestRepository correctionRepository;
    private final AbsencePlanRepository absencePlanRepository;
    private final EmployeeRepository employeeRepository;
    private final CalendarService calendarService;

    public PayrollService(MonthlyWorkReportRepository monthlyWorkReportRepository,
                          TimesheetPeriodControlRepository periodRepository,
                          DailyWorkReportRepository dailyWorkReportRepository,
                          AbsenceRepository absenceRepository,
                          AttendanceCorrectionRequestRepository correctionRepository,
                          AbsencePlanRepository absencePlanRepository,
                          EmployeeRepository employeeRepository,
                          CalendarService calendarService) {
        this.monthlyWorkReportRepository = monthlyWorkReportRepository;
        this.periodRepository = periodRepository;
        this.dailyWorkReportRepository = dailyWorkReportRepository;
        this.absenceRepository = absenceRepository;
        this.correctionRepository = correctionRepository;
        this.absencePlanRepository = absencePlanRepository;
        this.employeeRepository = employeeRepository;
        this.calendarService = calendarService;
    }

    public int calculateEstimatedMinutes(LocalDate startDate, LocalDate endDate) {
        return calendarService.getWorkingDaysList(startDate, endDate).size() * 480;
    }

    private int calculateDebtAdjustment(Long employeeId, LocalDate firstDayCurrentMonth) {
        LocalDate lastMonthEndDate = firstDayCurrentMonth.minusDays(1);
        LocalDate firstDayLastMonth = lastMonthEndDate.withDayOfMonth(1);

        Optional<TimesheetPeriodControl> lastPeriodControl = periodRepository.findByMonthAndYear(firstDayLastMonth.getMonthValue(), firstDayLastMonth.getYear());
        if (lastPeriodControl.isEmpty()) {
            return 0;
        }

        Optional<MonthlyWorkReport> lastMonthReport = monthlyWorkReportRepository.findByEmployeeIdAndPeriodStart(employeeId, firstDayLastMonth);
        if (lastMonthReport.isEmpty() || lastMonthReport.get().getEstimatedMinutes() == null || lastMonthReport.get().getEstimatedMinutes() <= 0) {
            return 0;
        }

        LocalDate startCheck = lastPeriodControl.get().getClosingDate();
        LocalDate endCheck = lastMonthEndDate;

        List<DailyWorkReport> actualReports = dailyWorkReportRepository.findByEmployeeIdAndWorkDateBetween(employeeId, startCheck, endCheck);
        List<LocalDate> correctedDates = correctionRepository.findByEmployeeIdAndWorkDateBetween(employeeId, startCheck, endCheck).stream()
                .filter(item -> item.getStatus() == ApprovalStatus.APPROVED)
                .map(AttendanceCorrectionRequest::getWorkDate)
                .toList();

        int standardWorkMinutes = 0;
        for (DailyWorkReport report : actualReports) {
            if (correctedDates.contains(report.getWorkDate())) {
                standardWorkMinutes += 480;
            } else {
                standardWorkMinutes += Math.min(report.getWorkTimeMinutes() == null ? 0 : report.getWorkTimeMinutes(), 480);
            }
        }

        long paidLeaveCount = absenceRepository.findByEmployeeId(employeeId).stream()
                .filter(absence -> absence.getWorkDate() != null && !absence.getWorkDate().isBefore(startCheck) && !absence.getWorkDate().isAfter(endCheck) && Boolean.TRUE.equals(absence.getIsPaid()))
                .count();

        int paidMinutes = (int) (paidLeaveCount * 480L);
        return standardWorkMinutes + paidMinutes - lastMonthReport.get().getEstimatedMinutes();
    }

    private PayrollMetrics calculateActualMetrics(Long employeeId, LocalDate startDate, LocalDate endDate) {
        PayrollMetrics metrics = new PayrollMetrics();
        List<DailyWorkReport> actualReports = dailyWorkReportRepository.findByEmployeeIdAndWorkDateBetween(employeeId, startDate, endDate.minusDays(1));

        for (DailyWorkReport report : actualReports) {
            boolean hasCorrection = correctionRepository.findByEmployeeId(employeeId).stream()
                    .anyMatch(request -> request.getWorkDate() != null && request.getWorkDate().equals(report.getWorkDate()) && request.getStatus() == ApprovalStatus.APPROVED);

            if (hasCorrection) {
                metrics.standardWorkMinutes += 480;
                metrics.actualWorkDays += 1;
                continue;
            }

            int dailyStandard = Math.min(report.getWorkTimeMinutes() == null ? 0 : report.getWorkTimeMinutes(), 480);
            metrics.standardWorkMinutes += dailyStandard;
            metrics.lackMinutes += report.getLackMinutes() == null ? 0 : report.getLackMinutes();
            if (dailyStandard > 0) {
                metrics.actualWorkDays += 1;
            }
        }

        for (Absence absence : absenceRepository.findByEmployeeId(employeeId)) {
            if (absence.getWorkDate() != null && !absence.getWorkDate().isBefore(startDate) && absence.getWorkDate().isBefore(endDate)) {
                if (Boolean.TRUE.equals(absence.getIsPaid())) {
                    metrics.paidLeaveDays += 1;
                } else {
                    metrics.unpaidLeaveDays += 1;
                }
            }
        }
        return metrics;
    }

    @Transactional
    public MonthlyWorkReport closeMonthlyPayroll(Long employeeId, int closingDay, int month, int year) {
        LocalDate closingDate = LocalDate.of(year, month, closingDay);
        LocalDate firstDay = closingDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = closingDate.withDayOfMonth(closingDate.lengthOfMonth());

        boolean isSpecialLeave = absencePlanRepository.findAll().stream().anyMatch(plan ->
                plan.getEmployeeId() != null && plan.getEmployeeId().equals(employeeId)
                        && plan.getStatus() == ApprovalStatus.APPROVED
                        && !plan.getStartDate().isAfter(lastDayOfMonth)
                        && !plan.getEndDate().isBefore(firstDay)
                        && plan.getAbsenceType() == AbsenceType.MATERNITY);
        if (isSpecialLeave) {
            return null;
        }

        PayrollMetrics metrics = calculateActualMetrics(employeeId, firstDay, closingDate);
        int debtAdjustment = calculateDebtAdjustment(employeeId, firstDay);
        int adjustedTotal = metrics.standardWorkMinutes + debtAdjustment;
        int estimatedMinutes = calculateEstimatedMinutes(closingDate, lastDayOfMonth);

        MonthlyWorkReport report = new MonthlyWorkReport();
        report.setEmployeeId(employeeId);
        report.setPeriodStart(firstDay);
        report.setPeriodEnd(closingDate);
        report.setStandardWorkMinutes(adjustedTotal);
        report.setLackMinutes(metrics.lackMinutes);
        report.setEstimatedMinutes(estimatedMinutes);
        report.setActualWorkDays(metrics.actualWorkDays);
        report.setPaidLeaveDays(metrics.paidLeaveDays);
        report.setUnpaidLeaveDays(metrics.unpaidLeaveDays);
        report.setCreatedAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());
        return monthlyWorkReportRepository.save(report);
    }

    @Transactional
    public Map<String, Object> calculateAllEmployeesPayroll(int closingDay, int month, int year) {
        Optional<TimesheetPeriodControl> periodControl = periodRepository.findByMonthAndYear(month, year);
        if (periodControl.isPresent() && Boolean.TRUE.equals(periodControl.get().getIsLocked())) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Kỳ công tháng " + month + "/" + year + " đã bị khóa. Không thể tính toán lại.");
        }

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        monthlyWorkReportRepository.deleteByPeriodStart(firstDayOfMonth);

        List<Employee> employees = employeeRepository.findAll().stream()
                .filter(employee -> employee.getIsActive() == null || Boolean.TRUE.equals(employee.getIsActive()))
                .toList();

        int processedCount = 0;
        int skippedCount = 0;
        for (Employee employee : employees) {
            MonthlyWorkReport result = closeMonthlyPayroll(employee.getId(), closingDay, month, year);
            if (result != null) {
                processedCount += 1;
            } else {
                skippedCount += 1;
            }
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("status", "success");
        payload.put("period", month + "/" + year);
        payload.put("total_employees", employees.size());
        payload.put("processed", processedCount);
        payload.put("skipped_special_leave", skippedCount);
        payload.put("message", "Dữ liệu cũ đã được làm mới và tính toán lại thành công.");
        return payload;
    }

    @Transactional
    public TimesheetPeriodControl lockTimesheetPeriod(Long adminId, int month, int year, int closingDay) {
        TimesheetPeriodControl existing = periodRepository.findByMonthAndYear(month, year).orElse(null);
        if (existing != null && Boolean.TRUE.equals(existing.getIsLocked())) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Kỳ công tháng " + month + "/" + year + " đã được khóa trước đó.");
        }

        TimesheetPeriodControl control = existing != null ? existing : new TimesheetPeriodControl();
        control.setMonth(month);
        control.setYear(year);
        control.setClosingDate(LocalDate.of(year, month, closingDay));
        control.setIsLocked(true);
        control.setLockedBy(adminId);
        control.setLockedAt(LocalDateTime.now());
        control.setNote("Chốt công tháng " + month + " năm " + year + " vào ngày " + closingDay);
        return periodRepository.save(control);
    }

    public Map<String, Object> getMonthlyReports(int page, int limit, String employeeName) {
        Pageable pageable = PageRequest.of(Math.max(page, 1) - 1, Math.max(limit, 1));
        Page<MonthlyWorkReport> reportPage = monthlyWorkReportRepository.findAll(pageable);

        List<MonthLyWorkReportResponse> data = new ArrayList<>();
        for (MonthlyWorkReport report : reportPage.getContent()) {
            Employee employee = employeeRepository.findById(report.getEmployeeId()).orElse(null);
            if (employeeName != null && !employeeName.isBlank() && (employee == null || employee.getFullName() == null || !employee.getFullName().toLowerCase().contains(employeeName.toLowerCase()))) {
                continue;
            }
            MonthLyWorkReportResponse dto = new MonthLyWorkReportResponse();
            dto.setId(report.getId());
            dto.setEmployeeId(report.getEmployeeId());
            dto.setEmployeeName(employee != null ? employee.getFullName() : null);
            dto.setEmail(employee != null ? employee.getEmail() : null);
            dto.setDepartmentId(employee != null ? employee.getDepartmentId() : null);
            dto.setPeriodStart(report.getPeriodStart());
            dto.setPeriodEnd(report.getPeriodEnd());
            dto.setStandardWorkMinutes(report.getStandardWorkMinutes());
            dto.setLackMinutes(report.getLackMinutes());
            dto.setEstimatedMinutes(report.getEstimatedMinutes());
            dto.setActualWorkDays(report.getActualWorkDays());
            dto.setPaidLeaveDays(report.getPaidLeaveDays());
            dto.setUnpaidLeaveDays(report.getUnpaidLeaveDays());
            data.add(dto);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", data);
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("total_elements", reportPage.getTotalElements());
        pagination.put("total_pages", reportPage.getTotalPages());
        pagination.put("page", page);
        pagination.put("limit", limit);
        result.put("pagination", pagination);
        return result;
    }

    public TimesheetPeriodControl getTimesheetPeriod(int month, int year) {
        return periodRepository.findByMonthAndYear(month, year).orElse(null);
    }

    public byte[] exportPayrollExcel(int month, int year) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Bang cong");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Mã NV");
        header.createCell(2).setCellValue("Tên");
        header.createCell(3).setCellValue("Email");
        header.createCell(4).setCellValue("Phòng ban");
        header.createCell(5).setCellValue("Kỳ công");
        header.createCell(6).setCellValue("Công thực tế");

        List<MonthlyWorkReport> reports = monthlyWorkReportRepository.findAll();
        for (int i = 0; i < reports.size(); i++) {
            MonthlyWorkReport report = reports.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(report.getId() != null ? report.getId() : 0);
            row.createCell(1).setCellValue(report.getEmployeeId() != null ? report.getEmployeeId() : 0);
            Employee employee = employeeRepository.findById(report.getEmployeeId()).orElse(null);
            row.createCell(2).setCellValue(employee != null ? employee.getFullName() : "");
            row.createCell(3).setCellValue(employee != null ? employee.getEmail() : "");
            row.createCell(4).setCellValue(employee != null && employee.getDepartmentId() != null ? employee.getDepartmentId() : 0);
            row.createCell(5).setCellValue(report.getPeriodStart() + " - " + report.getPeriodEnd());
            row.createCell(6).setCellValue(report.getStandardWorkMinutes() != null ? report.getStandardWorkMinutes() : 0);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("Không thể tạo file Excel", ex);
        }
    }

    private static class PayrollMetrics {
        private int standardWorkMinutes;
        private int lackMinutes;
        private int actualWorkDays;
        private int paidLeaveDays;
        private int unpaidLeaveDays;
    }
}
