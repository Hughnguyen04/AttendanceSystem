package com.attendance.controller;

import com.attendance.dto.TimesheetPeriodControlDTO;
import com.attendance.service.TimesheetPeriodControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/timesheet-periods")
@CrossOrigin(origins = "*")
public class TimesheetPeriodControlController {

    @Autowired
    private TimesheetPeriodControlService timesheetPeriodControlService;

    @GetMapping
    public ResponseEntity<List<TimesheetPeriodControlDTO>> getAllPeriods() {
        return ResponseEntity.ok(timesheetPeriodControlService.getAllPeriods());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimesheetPeriodControlDTO> getPeriodById(@PathVariable Integer id) {
        return ResponseEntity.ok(timesheetPeriodControlService.getPeriodById(id));
    }

    @PostMapping
    public ResponseEntity<TimesheetPeriodControlDTO> createPeriod(@RequestBody TimesheetPeriodControlDTO dto) {
        return ResponseEntity.status(201).body(timesheetPeriodControlService.createPeriod(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimesheetPeriodControlDTO> updatePeriod(
            @PathVariable Integer id,
            @RequestBody TimesheetPeriodControlDTO dto) {
        return ResponseEntity.ok(timesheetPeriodControlService.updatePeriod(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePeriod(@PathVariable Integer id) {
        timesheetPeriodControlService.deletePeriod(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TimesheetPeriodControlDTO>> getPeriodsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(timesheetPeriodControlService.getPeriodsByStatus(status));
    }
}
