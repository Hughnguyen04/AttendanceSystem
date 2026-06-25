package com.attendance.controller;

import com.attendance.dto.MonthlyReportDTO;
import com.attendance.service.MonthlyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/monthly-reports")
@CrossOrigin(origins = "*")
public class MonthlyReportController {

    @Autowired
    private MonthlyReportService monthlyReportService;

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<MonthlyReportDTO>> getReportsByEmployeeId(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(monthlyReportService.getReportsByEmployeeId(employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonthlyReportDTO> getReportById(@PathVariable Integer id) {
        return ResponseEntity.ok(monthlyReportService.getReportById(id));
    }

    @GetMapping("/employee/{employeeId}/period")
    public ResponseEntity<MonthlyReportDTO> getReportByPeriod(
            @PathVariable Integer employeeId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return ResponseEntity.ok(monthlyReportService.getReportByEmployeeAndPeriod(employeeId, month, year));
    }

    @PostMapping
    public ResponseEntity<MonthlyReportDTO> createReport(@RequestBody MonthlyReportDTO dto) {
        return ResponseEntity.status(201).body(monthlyReportService.createReport(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonthlyReportDTO> updateReport(
            @PathVariable Integer id,
            @RequestBody MonthlyReportDTO dto) {
        return ResponseEntity.ok(monthlyReportService.updateReport(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Integer id) {
        monthlyReportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/period")
    public ResponseEntity<List<MonthlyReportDTO>> getReportsByYearAndMonth(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        return ResponseEntity.ok(monthlyReportService.getReportsByYearAndMonth(year, month));
    }
}
