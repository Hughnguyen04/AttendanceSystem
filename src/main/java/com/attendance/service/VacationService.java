package com.attendance.service;

import com.attendance.model.Vacation;
import com.attendance.dto.VacationDTO;
import com.attendance.repository.VacationRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacationService {

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<VacationDTO> getVacationsByEmployeeId(Integer employeeId) {
        return vacationRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId)
                .stream()
                .map(v -> modelMapper.map(v, VacationDTO.class))
                .collect(Collectors.toList());
    }

    public VacationDTO getVacationById(Integer id) {
        Vacation vacation = vacationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation not found with id: " + id));
        return modelMapper.map(vacation, VacationDTO.class);
    }

    public VacationDTO createVacation(VacationDTO dto) {
        Vacation vacation = modelMapper.map(dto, Vacation.class);
        vacation.setCreatedAt(LocalDate.now());
        vacation.setStatus("pending");
        Vacation saved = vacationRepository.save(vacation);
        return modelMapper.map(saved, VacationDTO.class);
    }

    public VacationDTO updateVacation(Integer id, VacationDTO dto) {
        Vacation vacation = vacationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation not found with id: " + id));
        
        if (dto.getStartDate() != null) vacation.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) vacation.setEndDate(dto.getEndDate());
        if (dto.getVacationType() != null) vacation.setVacationType(dto.getVacationType());
        if (dto.getReason() != null) vacation.setReason(dto.getReason());
        if (dto.getStatus() != null) vacation.setStatus(dto.getStatus());
        if (dto.getApprovedBy() != null) vacation.setApprovedBy(dto.getApprovedBy());
        
        Vacation updated = vacationRepository.save(vacation);
        return modelMapper.map(updated, VacationDTO.class);
    }

    public void deleteVacation(Integer id) {
        vacationRepository.deleteById(id);
    }

    public List<VacationDTO> getVacationByStatus(String status, LocalDate startDate, LocalDate endDate) {
        return vacationRepository.findByStatusAndStartDateBetween(status, startDate, endDate)
                .stream()
                .map(v -> modelMapper.map(v, VacationDTO.class))
                .collect(Collectors.toList());
    }
}
