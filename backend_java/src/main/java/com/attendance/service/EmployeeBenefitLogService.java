package com.attendance.service;

import com.attendance.model.EmployeeBenefitLog;
import com.attendance.dto.EmployeeBenefitLogDTO;
import com.attendance.repository.EmployeeBenefitLogRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeBenefitLogService {

    @Autowired
    private EmployeeBenefitLogRepository employeeBenefitLogRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<EmployeeBenefitLogDTO> getBenefitsByEmployeeId(Integer employeeId) {
        return employeeBenefitLogRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId)
                .stream()
                .map(b -> modelMapper.map(b, EmployeeBenefitLogDTO.class))
                .collect(Collectors.toList());
    }

    public EmployeeBenefitLogDTO getBenefitById(Integer id) {
        EmployeeBenefitLog benefit = employeeBenefitLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee benefit log not found with id: " + id));
        return modelMapper.map(benefit, EmployeeBenefitLogDTO.class);
    }

    public EmployeeBenefitLogDTO createBenefit(EmployeeBenefitLogDTO dto) {
        EmployeeBenefitLog benefit = modelMapper.map(dto, EmployeeBenefitLogDTO.class);
        benefit.setCreatedAt(LocalDate.now());
        EmployeeBenefitLog saved = employeeBenefitLogRepository.save(benefit);
        return modelMapper.map(saved, EmployeeBenefitLogDTO.class);
    }

    public EmployeeBenefitLogDTO updateBenefit(Integer id, EmployeeBenefitLogDTO dto) {
        EmployeeBenefitLog benefit = employeeBenefitLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee benefit log not found with id: " + id));
        
        if (dto.getBenefitType() != null) benefit.setBenefitType(dto.getBenefitType());
        if (dto.getAmount() != null) benefit.setAmount(dto.getAmount());
        if (dto.getDescription() != null) benefit.setDescription(dto.getDescription());
        
        EmployeeBenefitLog updated = employeeBenefitLogRepository.save(benefit);
        return modelMapper.map(updated, EmployeeBenefitLogDTO.class);
    }

    public void deleteBenefit(Integer id) {
        employeeBenefitLogRepository.deleteById(id);
    }
}
