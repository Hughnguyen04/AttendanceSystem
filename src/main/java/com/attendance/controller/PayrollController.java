package com.attendance.controller;

import com.attendance.dto.PayrollDTO;
import com.attendance.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/payroll")
@CrossOrigin(origins = "*")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PayrollDTO>> getPayrollByEmployeeId(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(payrollService.getPayrollByEmployeeId(employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayrollDTO> getPayrollById(@PathVariable Integer id) {
        return ResponseEntity.ok(payrollService.getPayrollById(id));
    }

    @PostMapping
    public ResponseEntity<PayrollDTO> createPayroll(@RequestBody PayrollDTO dto) {
        return ResponseEntity.status(201).body(payrollService.createPayroll(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayrollDTO> updatePayroll(
            @PathVariable Integer id,
            @RequestBody PayrollDTO dto) {
        return ResponseEntity.ok(payrollService.updatePayroll(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayroll(@PathVariable Integer id) {
        payrollService.deletePayroll(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PayrollDTO>> getPayrollByStatus(@PathVariable String status) {
        return ResponseEntity.ok(payrollService.getPayrollByStatus(status));
    }
}
