package com.example.attendance.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.dto.PayrollCalculateRequest;
import com.example.attendance.entity.TimesheetPeriodControl;
import com.example.attendance.service.PayrollService;

@RestController
@RequestMapping("/payroll")
@CrossOrigin(origins = "*")
public class PayrollController {
    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping("/calculate-batch")
    public ResponseEntity<Map<String, Object>> calculateBatch(@RequestBody PayrollCalculateRequest request) {
        try {
            Map<String, Object> data = payrollService.calculateAllEmployeesPayroll(
                    request.getClosingDay() == null ? 20 : request.getClosingDay(),
                    request.getMonth() == null ? 1 : request.getMonth(),
                    request.getYear() == null ? java.time.LocalDate.now().getYear() : request.getYear());
            return okResponse(data);
        } catch (Exception ex) {
            return errorResponse(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/lock-period")
    public ResponseEntity<Map<String, Object>> lockPeriod(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "20") int closing_day) {
        try {
            TimesheetPeriodControl result = payrollService.lockTimesheetPeriod(1L, month, year, closing_day);
            return okResponse(result);
        } catch (Exception ex) {
            return errorResponse(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/period-control")
    public ResponseEntity<Map<String, Object>> getPeriodControl(@RequestParam int month, @RequestParam int year) {
        try {
            return okResponse(payrollService.getTimesheetPeriod(month, year));
        } catch (Exception ex) {
            return errorResponse(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/monthly-reports")
    public ResponseEntity<Map<String, Object>> getMonthlyReports(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int limit,
            @RequestParam(required = false) String employee_name) {
        try {
            Map<String, Object> data = payrollService.getMonthlyReports(page, limit, employee_name);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1000);
            response.put("message", "OK");
            response.put("data", data.get("data"));
            response.put("pagination", data.get("pagination"));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return errorResponse(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(@RequestParam(required = false) Integer month, @RequestParam(required = false) Integer year) {
        try {
            byte[] bytes = payrollService.exportPayrollExcel(month == null ? java.time.LocalDate.now().getMonthValue() : month,
                    year == null ? java.time.LocalDate.now().getYear() : year);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=bang-cong.xlsx")
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .body(bytes);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
        }
    }

    private ResponseEntity<Map<String, Object>> okResponse(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 1000);
        response.put("message", "OK");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> errorResponse(Exception ex, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("message", ex.getMessage());
        response.put("data", null);
        return ResponseEntity.status(status).body(response);
    }
}
