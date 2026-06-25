package com.attendance.service;

import com.attendance.model.DailyWorkReport;
import com.attendance.dto.DailyWorkReportDTO;
import com.attendance.repository.DailyWorkReportRepository;
import com.attendance.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyWorkReportService {

    @Autowired
    private DailyWorkReportRepository dailyWorkReportRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<DailyWorkReportDTO> getReportsByEmployeeId(Integer employeeId) {
        return dailyWorkReportRepository.findByEmployeeIdOrderByWorkDateDesc(employeeId)
                .stream()
                .map(r -> modelMapper.map(r, DailyWorkReportDTO.class))
                .collect(Collectors.toList());
    }

    public DailyWorkReportDTO getReportById(Integer id) {
        DailyWorkReport report = dailyWorkReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Daily work report not found with id: " + id));
        return modelMapper.map(report, DailyWorkReportDTO.class);
    }

    public DailyWorkReportDTO createReport(DailyWorkReportDTO dto) {
        DailyWorkReport report = modelMapper.map(dto, DailyWorkReport.class);
        report.setCreatedAt(LocalDate.now());
        DailyWorkReport saved = dailyWorkReportRepository.save(report);
        return modelMapper.map(saved, DailyWorkReportDTO.class);
    }

    public DailyWorkReportDTO updateReport(Integer id, DailyWorkReportDTO dto) {
        DailyWorkReport report = dailyWorkReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Daily work report not found with id: " + id));
        
        if (dto.getWorkSummary() != null) report.setWorkSummary(dto.getWorkSummary());
        if (dto.getAchievements() != null) report.setAchievements(dto.getAchievements());
        if (dto.getChallenges() != null) report.setChallenges(dto.getChallenges());
        if (dto.getNextDayPlan() != null) report.setNextDayPlan(dto.getNextDayPlan());
        if (dto.getProductivityScore() != null) report.setProductivityScore(dto.getProductivityScore());
        
        DailyWorkReport updated = dailyWorkReportRepository.save(report);
        return modelMapper.map(updated, DailyWorkReportDTO.class);
    }

    public void deleteReport(Integer id) {
        dailyWorkReportRepository.deleteById(id);
    }

    public DailyWorkReportDTO getReportByDate(Integer employeeId, LocalDate workDate) {
        DailyWorkReport report = dailyWorkReportRepository.findByEmployeeIdAndWorkDate(employeeId, workDate)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found for date: " + workDate));
        return modelMapper.map(report, DailyWorkReportDTO.class);
    }
}
