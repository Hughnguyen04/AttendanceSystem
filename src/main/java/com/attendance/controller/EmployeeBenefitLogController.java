package com.attendance.controller;

import com.attendance.dto.EmployeeBenefitLogDTO;
import com.attendance.service.EmployeeBenefitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/employee-benefits")
@CrossOrigin(origins = "*")
public class EmployeeBenefitLogController {

    @Autowired
    private EmployeeBenefitLogService employeeBenefitLogService;

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeBenefitLogDTO>> getBenefitsByEmployeeId(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(employeeBenefitLogService.getBenefitsByEmployeeId(employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeBenefitLogDTO> getBenefitById(@PathVariable Integer id) {
        return ResponseEntity.ok(employeeBenefitLogService.getBenefitById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeBenefitLogDTO> createBenefit(@RequestBody EmployeeBenefitLogDTO dto) {
        return ResponseEntity.status(201).body(employeeBenefitLogService.createBenefit(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeBenefitLogDTO> updateBenefit(
            @PathVariable Integer id,
            @RequestBody EmployeeBenefitLogDTO dto) {
        return ResponseEntity.ok(employeeBenefitLogService.updateBenefit(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBenefit(@PathVariable Integer id) {
        employeeBenefitLogService.deleteBenefit(id);
        return ResponseEntity.noContent().build();
    }
}
