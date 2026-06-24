package com.attendance.controller;

import com.attendance.dto.AbsenceDTO;
import com.attendance.service.AbsenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/absences")
@CrossOrigin(origins = "*")
public class AbsenceController {

    @Autowired
    private AbsenceService absenceService;

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AbsenceDTO>> getAbsencesByEmployeeId(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(absenceService.getAbsencesByEmployeeId(employeeId));
    }

    @GetMapping("/employee/{employeeId}/approved")
    public ResponseEntity<List<AbsenceDTO>> getApprovedAbsencesByEmployeeId(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(absenceService.getApprovedAbsencesByEmployeeId(employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbsenceDTO> getAbsenceById(@PathVariable Integer id) {
        return ResponseEntity.ok(absenceService.getAbsenceById(id));
    }

    @PostMapping
    public ResponseEntity<AbsenceDTO> createAbsence(@RequestBody AbsenceDTO dto) {
        return ResponseEntity.status(201).body(absenceService.createAbsence(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AbsenceDTO> updateAbsence(
            @PathVariable Integer id,
            @RequestBody AbsenceDTO dto) {
        return ResponseEntity.ok(absenceService.updateAbsence(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAbsence(@PathVariable Integer id) {
        absenceService.deleteAbsence(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AbsenceDTO>> getAbsencesByStatus(
            @PathVariable String status,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(absenceService.getAbsencesByStatus(status, startDate, endDate));
    }
}
