package com.attendance.service;

import com.attendance.model.AttendanceCorrection;
import com.attendance.dto.AttendanceCorrectionDTO;
import com.attendance.repository.AttendanceCorrectionRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceCorrectionService {

    @Autowired
    private AttendanceCorrectionRepository attendanceCorrectionRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<AttendanceCorrectionDTO> getCorrectionsByEmployeeId(Integer employeeId) {
        return attendanceCorrectionRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId)
                .stream()
                .map(a -> modelMapper.map(a, AttendanceCorrectionDTO.class))
                .collect(Collectors.toList());
    }

    public AttendanceCorrectionDTO getCorrectionById(Integer id) {
        AttendanceCorrection correction = attendanceCorrectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance correction not found with id: " + id));
        return modelMapper.map(correction, AttendanceCorrectionDTO.class);
    }

    public AttendanceCorrectionDTO createCorrection(AttendanceCorrectionDTO dto) {
        AttendanceCorrection correction = modelMapper.map(dto, AttendanceCorrectionDTO.class);
        correction.setCreatedAt(LocalDate.now());
        correction.setStatus("pending");
        AttendanceCorrection saved = attendanceCorrectionRepository.save(correction);
        return modelMapper.map(saved, AttendanceCorrectionDTO.class);
    }

    public AttendanceCorrectionDTO updateCorrection(Integer id, AttendanceCorrectionDTO dto) {
        AttendanceCorrection correction = attendanceCorrectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance correction not found with id: " + id));
        
        if (dto.getCorrectionType() != null) correction.setCorrectionType(dto.getCorrectionType());
        if (dto.getCorrectedCheckIn() != null) correction.setCorrectedCheckIn(dto.getCorrectedCheckIn());
        if (dto.getCorrectedCheckOut() != null) correction.setCorrectedCheckOut(dto.getCorrectedCheckOut());
        if (dto.getReason() != null) correction.setReason(dto.getReason());
        if (dto.getStatus() != null) correction.setStatus(dto.getStatus());
        
        AttendanceCorrection updated = attendanceCorrectionRepository.save(correction);
        return modelMapper.map(updated, AttendanceCorrectionDTO.class);
    }

    public void deleteCorrection(Integer id) {
        attendanceCorrectionRepository.deleteById(id);
    }

    public List<AttendanceCorrectionDTO> getCorrectionsByStatus(String status) {
        return attendanceCorrectionRepository.findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(a -> modelMapper.map(a, AttendanceCorrectionDTO.class))
                .collect(Collectors.toList());
    }
}
