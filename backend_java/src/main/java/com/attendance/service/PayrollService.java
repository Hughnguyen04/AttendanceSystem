package com.attendance.service;

import com.attendance.model.Payroll;
import com.attendance.dto.PayrollDTO;
import com.attendance.repository.PayrollRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<PayrollDTO> getPayrollByEmployeeId(Integer employeeId) {
        return payrollRepository.findByEmployeeIdOrderByPeriodStartDesc(employeeId)
                .stream()
                .map(p -> modelMapper.map(p, PayrollDTO.class))
                .collect(Collectors.toList());
    }

    public PayrollDTO getPayrollById(Integer id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found with id: " + id));
        return modelMapper.map(payroll, PayrollDTO.class);
    }

    public PayrollDTO createPayroll(PayrollDTO dto) {
        Payroll payroll = modelMapper.map(dto, Payroll.class);
        payroll.setCreatedAt(LocalDate.now());
        payroll.setStatus("draft");
        Payroll saved = payrollRepository.save(payroll);
        return modelMapper.map(saved, PayrollDTO.class);
    }

    public PayrollDTO updatePayroll(Integer id, PayrollDTO dto) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found with id: " + id));
        
        if (dto.getBaseSalary() != null) payroll.setBaseSalary(dto.getBaseSalary());
        if (dto.getOvertimeAmount() != null) payroll.setOvertimeAmount(dto.getOvertimeAmount());
        if (dto.getDeductions() != null) payroll.setDeductions(dto.getDeductions());
        if (dto.getNetSalary() != null) payroll.setNetSalary(dto.getNetSalary());
        if (dto.getStatus() != null) payroll.setStatus(dto.getStatus());
        
        Payroll updated = payrollRepository.save(payroll);
        return modelMapper.map(updated, PayrollDTO.class);
    }

    public void deletePayroll(Integer id) {
        payrollRepository.deleteById(id);
    }

    public List<PayrollDTO> getPayrollByStatus(String status) {
        return payrollRepository.findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(p -> modelMapper.map(p, PayrollDTO.class))
                .collect(Collectors.toList());
    }
}
