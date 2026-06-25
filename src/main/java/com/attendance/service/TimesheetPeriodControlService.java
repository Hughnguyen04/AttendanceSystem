package com.attendance.service;

import com.attendance.model.TimesheetPeriodControl;
import com.attendance.dto.TimesheetPeriodControlDTO;
import com.attendance.repository.TimesheetPeriodControlRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimesheetPeriodControlService {

    @Autowired
    private TimesheetPeriodControlRepository timesheetPeriodControlRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<TimesheetPeriodControlDTO> getAllPeriods() {
        return timesheetPeriodControlRepository.findAll()
                .stream()
                .map(t -> modelMapper.map(t, TimesheetPeriodControlDTO.class))
                .collect(Collectors.toList());
    }

    public TimesheetPeriodControlDTO getPeriodById(Integer id) {
        TimesheetPeriodControl period = timesheetPeriodControlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timesheet period not found with id: " + id));
        return modelMapper.map(period, TimesheetPeriodControlDTO.class);
    }

    public TimesheetPeriodControlDTO createPeriod(TimesheetPeriodControlDTO dto) {
        TimesheetPeriodControl period = modelMapper.map(dto, TimesheetPeriodControl.class);
        period.setCreatedAt(LocalDate.now());
        period.setIsLocked(false);
        TimesheetPeriodControl saved = timesheetPeriodControlRepository.save(period);
        return modelMapper.map(saved, TimesheetPeriodControlDTO.class);
    }

    public TimesheetPeriodControlDTO updatePeriod(Integer id, TimesheetPeriodControlDTO dto) {
        TimesheetPeriodControl period = timesheetPeriodControlRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timesheet period not found with id: " + id));
        
        if (dto.getPeriodName() != null) period.setPeriodName(dto.getPeriodName());
        if (dto.getStatus() != null) period.setStatus(dto.getStatus());
        if (dto.getIsLocked() != null) period.setIsLocked(dto.getIsLocked());
        
        TimesheetPeriodControl updated = timesheetPeriodControlRepository.save(period);
        return modelMapper.map(updated, TimesheetPeriodControlDTO.class);
    }

    public void deletePeriod(Integer id) {
        timesheetPeriodControlRepository.deleteById(id);
    }

    public List<TimesheetPeriodControlDTO> getPeriodsByStatus(String status) {
        return timesheetPeriodControlRepository.findByStatusOrderByPeriodStartDesc(status)
                .stream()
                .map(t -> modelMapper.map(t, TimesheetPeriodControlDTO.class))
                .collect(Collectors.toList());
    }
}
