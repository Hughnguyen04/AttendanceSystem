package com.attendance.controller;

import com.attendance.dto.DailyWorkReportDTO;
import com.attendance.service.DailyWorkReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/daily-reports")
@CrossOrigin(origins = "*")
public class DailyWorkReportController {

    @Autowired
    private DailyWorkReportService dailyWorkReportService;

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<DailyWorkReportDTO>> getReportsByEmployeeId(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(dailyWorkReportService.getReportsByEmployeeId(employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DailyWorkReportDTO> getReportById(@PathVariable Integer id) {
        return ResponseEntity.ok(dailyWorkReportService.getReportById(id));
    }

    @GetMapping("/employee/{employeeId}/date/{workDate}")
    public ResponseEntity<DailyWorkReportDTO> getReportByDate(
            @PathVariable Integer employeeId,
            @PathVariable LocalDate workDate) {
        return ResponseEntity.ok(dailyWorkReportService.getReportByDate(employeeId, workDate));
    }

    @PostMapping
    public ResponseEntity<DailyWorkReportDTO> createReport(@RequestBody DailyWorkReportDTO dto) {
        return ResponseEntity.status(201).body(dailyWorkReportService.createReport(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailyWorkReportDTO> updateReport(
            @PathVariable Integer id,
            @RequestBody DailyWorkReportDTO dto) {
        return ResponseEntity.ok(dailyWorkReportService.updateReport(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Integer id) {
        dailyWorkReportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
