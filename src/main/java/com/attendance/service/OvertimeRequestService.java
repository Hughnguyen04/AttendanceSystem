package com.attendance.service;

import com.attendance.model.OvertimeRequest;
import com.attendance.dto.OvertimeRequestDTO;
import com.attendance.repository.OvertimeRequestRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OvertimeRequestService {

    @Autowired
    private OvertimeRequestRepository overtimeRequestRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<OvertimeRequestDTO> getOvertimeByEmployeeId(Integer employeeId) {
        return overtimeRequestRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId)
                .stream()
                .map(o -> modelMapper.map(o, OvertimeRequestDTO.class))
                .collect(Collectors.toList());
    }

    public OvertimeRequestDTO getOvertimeById(Integer id) {
        OvertimeRequest overtime = overtimeRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Overtime request not found with id: " + id));
        return modelMapper.map(overtime, OvertimeRequestDTO.class);
    }

    public OvertimeRequestDTO createOvertimeRequest(OvertimeRequestDTO dto) {
        OvertimeRequest overtime = modelMapper.map(dto, OvertimeRequest.class);
        overtime.setCreatedAt(LocalDate.now());
        overtime.setStatus("pending");
        OvertimeRequest saved = overtimeRequestRepository.save(overtime);
        return modelMapper.map(saved, OvertimeRequestDTO.class);
    }

    public OvertimeRequestDTO updateOvertimeRequest(Integer id, OvertimeRequestDTO dto) {
        OvertimeRequest overtime = overtimeRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Overtime request not found with id: " + id));
        
        if (dto.getHours() != null) overtime.setHours(dto.getHours());
        if (dto.getReason() != null) overtime.setReason(dto.getReason());
        if (dto.getStatus() != null) overtime.setStatus(dto.getStatus());
        
        OvertimeRequest updated = overtimeRequestRepository.save(overtime);
        return modelMapper.map(updated, OvertimeRequestDTO.class);
    }

    public void deleteOvertimeRequest(Integer id) {
        overtimeRequestRepository.deleteById(id);
    }

    public List<OvertimeRequestDTO> getOvertimeByStatus(String status, LocalDate startDate, LocalDate endDate) {
        return overtimeRequestRepository.findByStatusAndWorkDateBetween(status, startDate, endDate)
                .stream()
                .map(o -> modelMapper.map(o, OvertimeRequestDTO.class))
                .collect(Collectors.toList());
    }
}
