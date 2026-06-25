package com.attendance.service;

import com.attendance.model.AttendanceLog;
import com.attendance.dto.AttendanceLogDTO;
import com.attendance.repository.AttendanceLogRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceLogService {

    @Autowired
    private AttendanceLogRepository attendanceLogRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<AttendanceLogDTO> getAttendanceByEmployeeId(Integer employeeId) {
        return attendanceLogRepository.findByEmployeeIdOrderByWorkDateDesc(employeeId)
                .stream()
                .map(a -> modelMapper.map(a, AttendanceLogDTO.class))
                .collect(Collectors.toList());
    }

    public AttendanceLogDTO getAttendanceById(Integer id) {
        AttendanceLog log = attendanceLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance log not found with id: " + id));
        return modelMapper.map(log, AttendanceLogDTO.class);
    }

    public AttendanceLogDTO checkIn(Integer employeeId, LocalDate workDate) {
        AttendanceLog log = new AttendanceLog();
        log.setEmployeeId(employeeId);
        log.setWorkDate(workDate);
        log.setCheckInTime(LocalTime.now());
        log.setStatus("Present");
        AttendanceLog saved = attendanceLogRepository.save(log);
        return modelMapper.map(saved, AttendanceLogDTO.class);
    }

    public AttendanceLogDTO checkOut(Integer employeeId, LocalDate workDate) {
        AttendanceLog log = attendanceLogRepository
                .findByEmployeeIdAndWorkDateOrderByCheckInTimeDesc(employeeId, workDate)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance log not found for check out"));
        log.setCheckOutTime(LocalTime.now());
        AttendanceLog updated = attendanceLogRepository.save(log);
        return modelMapper.map(updated, AttendanceLogDTO.class);
    }

    public AttendanceLogDTO createAttendanceLog(AttendanceLogDTO dto) {
        AttendanceLog log = modelMapper.map(dto, AttendanceLog.class);
        AttendanceLog saved = attendanceLogRepository.save(log);
        return modelMapper.map(saved, AttendanceLogDTO.class);
    }

    public AttendanceLogDTO updateAttendanceLog(Integer id, AttendanceLogDTO dto) {
        AttendanceLog log = attendanceLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance log not found with id: " + id));
        
        if (dto.getCheckInTime() != null) log.setCheckInTime(dto.getCheckInTime());
        if (dto.getCheckOutTime() != null) log.setCheckOutTime(dto.getCheckOutTime());
        if (dto.getStatus() != null) log.setStatus(dto.getStatus());
        
        AttendanceLog updated = attendanceLogRepository.save(log);
        return modelMapper.map(updated, AttendanceLogDTO.class);
    }

    public void deleteAttendanceLog(Integer id) {
        attendanceLogRepository.deleteById(id);
    }
}
