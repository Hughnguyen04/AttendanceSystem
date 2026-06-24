package com.attendance.service;

import com.attendance.model.MonthlyReport;
import com.attendance.dto.MonthlyReportDTO;
import com.attendance.repository.MonthlyReportRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonthlyReportService {

    @Autowired
    private MonthlyReportRepository monthlyReportRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<MonthlyReportDTO> getReportsByEmployeeId(Integer employeeId) {
        return monthlyReportRepository.findByEmployeeIdOrderByYearDescMonthDesc(employeeId)
                .stream()
                .map(m -> modelMapper.map(m, MonthlyReportDTO.class))
                .collect(Collectors.toList());
    }

    public MonthlyReportDTO getReportById(Integer id) {
        MonthlyReport report = monthlyReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monthly report not found with id: " + id));
        return modelMapper.map(report, MonthlyReportDTO.class);
    }

    public MonthlyReportDTO getReportByEmployeeAndPeriod(Integer employeeId, Integer month, Integer year) {
        MonthlyReport report = monthlyReportRepository.findByEmployeeIdAndMonthAndYear(employeeId, month, year)
                .orElseThrow(() -> new ResourceNotFoundException("Monthly report not found for employee: " + employeeId + ", month: " + month + ", year: " + year));
        return modelMapper.map(report, MonthlyReportDTO.class);
    }

    public MonthlyReportDTO createReport(MonthlyReportDTO dto) {
        MonthlyReport report = modelMapper.map(dto, MonthlyReportDTO.class);
        MonthlyReport saved = monthlyReportRepository.save(report);
        return modelMapper.map(saved, MonthlyReportDTO.class);
    }

    public MonthlyReportDTO updateReport(Integer id, MonthlyReportDTO dto) {
        MonthlyReport report = monthlyReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monthly report not found with id: " + id));
        
        if (dto.getTotalWorkingHours() != null) report.setTotalWorkingHours(dto.getTotalWorkingHours());
        if (dto.getOvertimeHours() != null) report.setOvertimeHours(dto.getOvertimeHours());
        if (dto.getAbsentDays() != null) report.setAbsentDays(dto.getAbsentDays());
        if (dto.getVacationDays() != null) report.setVacationDays(dto.getVacationDays());
        if (dto.getTotalSalary() != null) report.setTotalSalary(dto.getTotalSalary());
        
        MonthlyReport updated = monthlyReportRepository.save(report);
        return modelMapper.map(updated, MonthlyReportDTO.class);
    }

    public void deleteReport(Integer id) {
        monthlyReportRepository.deleteById(id);
    }

    public List<MonthlyReportDTO> getReportsByYearAndMonth(Integer year, Integer month) {
        return monthlyReportRepository.findByYearAndMonthOrderByEmployeeIdAsc(year, month)
                .stream()
                .map(m -> modelMapper.map(m, MonthlyReportDTO.class))
                .collect(Collectors.toList());
    }
}
