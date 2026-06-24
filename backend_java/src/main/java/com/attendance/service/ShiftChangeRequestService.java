package com.attendance.service;

import com.attendance.model.ShiftChangeRequest;
import com.attendance.dto.ShiftChangeRequestDTO;
import com.attendance.repository.ShiftChangeRequestRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShiftChangeRequestService {

    @Autowired
    private ShiftChangeRequestRepository shiftChangeRequestRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ShiftChangeRequestDTO> getRequestsByEmployeeId(Integer employeeId) {
        return shiftChangeRequestRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId)
                .stream()
                .map(s -> modelMapper.map(s, ShiftChangeRequestDTO.class))
                .collect(Collectors.toList());
    }

    public ShiftChangeRequestDTO getRequestById(Integer id) {
        ShiftChangeRequest request = shiftChangeRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift change request not found with id: " + id));
        return modelMapper.map(request, ShiftChangeRequestDTO.class);
    }

    public ShiftChangeRequestDTO createRequest(ShiftChangeRequestDTO dto) {
        ShiftChangeRequest request = modelMapper.map(dto, ShiftChangeRequestDTO.class);
        request.setCreatedAt(LocalDate.now());
        request.setStatus("pending");
        ShiftChangeRequest saved = shiftChangeRequestRepository.save(request);
        return modelMapper.map(saved, ShiftChangeRequestDTO.class);
    }

    public ShiftChangeRequestDTO updateRequest(Integer id, ShiftChangeRequestDTO dto) {
        ShiftChangeRequest request = shiftChangeRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift change request not found with id: " + id));
        
        if (dto.getRequestedShiftId() != null) request.setRequestedShiftId(dto.getRequestedShiftId());
        if (dto.getEffectiveDate() != null) request.setEffectiveDate(dto.getEffectiveDate());
        if (dto.getReason() != null) request.setReason(dto.getReason());
        if (dto.getStatus() != null) request.setStatus(dto.getStatus());
        
        ShiftChangeRequest updated = shiftChangeRequestRepository.save(request);
        return modelMapper.map(updated, ShiftChangeRequestDTO.class);
    }

    public void deleteRequest(Integer id) {
        shiftChangeRequestRepository.deleteById(id);
    }

    public List<ShiftChangeRequestDTO> getRequestsByStatus(String status) {
        return shiftChangeRequestRepository.findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(s -> modelMapper.map(s, ShiftChangeRequestDTO.class))
                .collect(Collectors.toList());
    }
}
