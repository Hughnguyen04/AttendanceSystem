package com.attendance.service;

import com.attendance.model.Absence;
import com.attendance.dto.AbsenceDTO;
import com.attendance.repository.AbsenceRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AbsenceService {

    @Autowired
    private AbsenceRepository absenceRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<AbsenceDTO> getAbsencesByEmployeeId(Integer employeeId) {
        return absenceRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId)
                .stream()
                .map(a -> modelMapper.map(a, AbsenceDTO.class))
                .collect(Collectors.toList());
    }

    public List<AbsenceDTO> getApprovedAbsencesByEmployeeId(Integer employeeId) {
        return absenceRepository.findByEmployeeIdAndStatusOrderByStartDateDesc(employeeId, "approved")
                .stream()
                .map(a -> modelMapper.map(a, AbsenceDTO.class))
                .collect(Collectors.toList());
    }

    public AbsenceDTO getAbsenceById(Integer id) {
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Absence not found with id: " + id));
        return modelMapper.map(absence, AbsenceDTO.class);
    }

    public AbsenceDTO createAbsence(AbsenceDTO dto) {
        Absence absence = modelMapper.map(dto, Absence.class);
        absence.setCreatedAt(LocalDate.now());
        absence.setStatus("pending");
        Absence saved = absenceRepository.save(absence);
        return modelMapper.map(saved, AbsenceDTO.class);
    }

    public AbsenceDTO updateAbsence(Integer id, AbsenceDTO dto) {
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Absence not found with id: " + id));
        
        if (dto.getAbsenceType() != null) absence.setAbsenceType(dto.getAbsenceType());
        if (dto.getStartDate() != null) absence.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) absence.setEndDate(dto.getEndDate());
        if (dto.getReason() != null) absence.setReason(dto.getReason());
        if (dto.getStatus() != null) absence.setStatus(dto.getStatus());
        
        Absence updated = absenceRepository.save(absence);
        return modelMapper.map(updated, AbsenceDTO.class);
    }

    public void deleteAbsence(Integer id) {
        absenceRepository.deleteById(id);
    }

    public List<AbsenceDTO> getAbsencesByStatus(String status, LocalDate startDate, LocalDate endDate) {
        return absenceRepository.findByStatusAndStartDateBetween(status, startDate, endDate)
                .stream()
                .map(a -> modelMapper.map(a, AbsenceDTO.class))
                .collect(Collectors.toList());
    }
}
