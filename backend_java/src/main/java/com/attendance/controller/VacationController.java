package com.attendance.controller;

import com.attendance.dto.VacationDTO;
import com.attendance.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vacations")
@CrossOrigin(origins = "*")
public class VacationController {

    @Autowired
    private VacationService vacationService;

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<VacationDTO>> getVacationsByEmployeeId(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(vacationService.getVacationsByEmployeeId(employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacationDTO> getVacationById(@PathVariable Integer id) {
        return ResponseEntity.ok(vacationService.getVacationById(id));
    }

    @PostMapping
    public ResponseEntity<VacationDTO> createVacation(@RequestBody VacationDTO dto) {
        return ResponseEntity.status(201).body(vacationService.createVacation(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacationDTO> updateVacation(
            @PathVariable Integer id,
            @RequestBody VacationDTO dto) {
        return ResponseEntity.ok(vacationService.updateVacation(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacation(@PathVariable Integer id) {
        vacationService.deleteVacation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<VacationDTO>> getVacationByStatus(
            @PathVariable String status,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(vacationService.getVacationByStatus(status, startDate, endDate));
    }
}
