package com.example.attendance.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.attendance.dto.VacationRequest;
import com.example.attendance.dto.VacationResponse;
import com.example.attendance.dto.WorkCompensationRequest;
import com.example.attendance.dto.WorkCompensationResponse;
import com.example.attendance.entity.Vacation;
import com.example.attendance.entity.VacationType;
import com.example.attendance.entity.WorkCompensation;
import com.example.attendance.repository.VacationRepository;
import com.example.attendance.repository.WorkCompensationRepository;

@Service
public class CalendarService {
    private final VacationRepository vacationRepository;
    private final WorkCompensationRepository workCompensationRepository;

    public CalendarService(VacationRepository vacationRepository, WorkCompensationRepository workCompensationRepository) {
        this.vacationRepository = vacationRepository;
        this.workCompensationRepository = workCompensationRepository;
    }

    @Transactional
    public VacationResponse createVacation(VacationRequest request) {
        validateVacationPeriod(request.getStartDate(), request.getEndDate());

        if (!vacationRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(request.getStartDate(), request.getEndDate()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thời gian này bị trùng với ngày nghỉ hiện có.");
        }

        Vacation entity = new Vacation();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setEmployeeId(request.getEmployeeId() == null ? 1L : request.getEmployeeId());
        entity.setStatus("PENDING");
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setVacationType(request.getVacationType() == null ? VacationType.HOLIDAY : request.getVacationType());
        entity.setIsPaid(request.getIsPaid() == null ? true : request.getIsPaid());
        entity.setIsRecurring(request.getIsRecurring() == null ? true : request.getIsRecurring());

        return toVacationResponse(vacationRepository.save(entity));
    }

    @Transactional
    public VacationResponse updateVacation(Long vacationId, VacationRequest request) {
        Vacation existing = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ngày nghỉ"));

        LocalDate startDate = request.getStartDate() == null ? existing.getStartDate() : request.getStartDate();
        LocalDate endDate = request.getEndDate() == null ? existing.getEndDate() : request.getEndDate();
        validateVacationPeriod(startDate, endDate);

        if (vacationRepository.existsByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIdNot(startDate, endDate, vacationId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cập nhật thất bại! Thời gian bị trùng với ngày nghỉ khác.");
        }

        existing.setTitle(request.getTitle() == null ? existing.getTitle() : request.getTitle());
        existing.setDescription(request.getDescription() == null ? existing.getDescription() : request.getDescription());
        existing.setStartDate(startDate);
        existing.setEndDate(endDate);
        existing.setVacationType(request.getVacationType() == null ? existing.getVacationType() : request.getVacationType());
        existing.setIsPaid(request.getIsPaid() == null ? existing.getIsPaid() : request.getIsPaid());
        existing.setIsRecurring(request.getIsRecurring() == null ? existing.getIsRecurring() : request.getIsRecurring());

        return toVacationResponse(vacationRepository.save(existing));
    }

    @Transactional
    public void deleteVacation(Long vacationId) {
        Vacation existing = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ngày nghỉ"));
        vacationRepository.delete(existing);
    }

    public List<VacationResponse> getVacations() {
        return vacationRepository.findAll().stream().map(this::toVacationResponse).collect(Collectors.toList());
    }

    public VacationResponse getVacationById(Long vacationId) {
        return toVacationResponse(vacationRepository.findById(vacationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ngày nghỉ này")));
    }

    public List<LocalDate> getWorkingDaysList(LocalDate start, LocalDate end) {
        List<LocalDate> days = new ArrayList<>();
        Set<LocalDate> compensationDates = new LinkedHashSet<>(workCompensationRepository.findByCompensateDateBetween(start, end).stream()
                .map(WorkCompensation::getCompensateDate)
                .collect(Collectors.toList()));

        LocalDate current = start;
        while (!current.isAfter(end)) {
            if (current.getDayOfWeek().getValue() < 6) {
                boolean isHoliday = false;
                for (Vacation vacation : vacationRepository.findAll()) {
                    LocalDate vacationStart = vacation.getStartDate();
                    LocalDate vacationEnd = vacation.getEndDate();

                    if (Boolean.TRUE.equals(vacation.getIsRecurring())) {
                        LocalDate recurringStart = vacationStart.withYear(current.getYear());
                        LocalDate recurringEnd = vacationEnd.withYear(current.getYear());
                        if (!current.isBefore(recurringStart) && !current.isAfter(recurringEnd)) {
                            isHoliday = true;
                            break;
                        }
                    } else {
                        if (!current.isBefore(vacationStart) && !current.isAfter(vacationEnd)) {
                            isHoliday = true;
                            break;
                        }
                    }
                }

                if (!isHoliday) {
                    days.add(current);
                }
            } else if (compensationDates.contains(current)) {
                days.add(current);
            }

            current = current.plusDays(1);
        }

        return days;
    }

    @Transactional
    public WorkCompensationResponse createCompensation(WorkCompensationRequest request) {
        LocalDate compensateDate = request.getCompensateDate();
        if (compensateDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày làm bù không được để trống.");
        }

        if (compensateDate.getDayOfWeek().getValue() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày làm bù phải được ấn định vào Thứ Bảy hoặc Chủ Nhật.");
        }

        if (!vacationRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(compensateDate, compensateDate).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể làm bù vào ngày này vì đang trùng với lịch nghỉ.");
        }

        if (workCompensationRepository.existsByCompensateDate(compensateDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày làm bù này đã có trong hệ thống.");
        }

        WorkCompensation entity = new WorkCompensation();
        entity.setTitle(request.getTitle());
        entity.setCompensateDate(compensateDate);
        entity.setDescription(request.getDescription());

        return toWorkCompensationResponse(workCompensationRepository.save(entity));
    }

    @Transactional
    public WorkCompensationResponse updateCompensation(Long compensationId, WorkCompensationRequest request) {
        WorkCompensation existing = workCompensationRepository.findById(compensationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ngày làm bù."));

        LocalDate newDate = request.getCompensateDate() == null ? existing.getCompensateDate() : request.getCompensateDate();
        if (newDate.getDayOfWeek().getValue() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày làm bù mới phải là Thứ Bảy hoặc Chủ Nhật.");
        }

        if (!vacationRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(newDate, newDate).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày mới trùng lịch nghỉ.");
        }

        if (workCompensationRepository.existsByCompensateDateAndIdNot(newDate, compensationId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày làm bù này đã tồn tại.");
        }

        existing.setTitle(request.getTitle() == null ? existing.getTitle() : request.getTitle());
        existing.setCompensateDate(newDate);
        existing.setDescription(request.getDescription() == null ? existing.getDescription() : request.getDescription());

        return toWorkCompensationResponse(workCompensationRepository.save(existing));
    }

    @Transactional
    public void deleteCompensation(Long compensationId) {
        WorkCompensation existing = workCompensationRepository.findById(compensationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ngày làm bù."));
        workCompensationRepository.delete(existing);
    }

    public List<WorkCompensationResponse> getCompensations() {
        return workCompensationRepository.findAll().stream().map(this::toWorkCompensationResponse).collect(Collectors.toList());
    }

    private void validateVacationPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "start_date và end_date không được để trống.");
        }
        if (endDate.isBefore(startDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày kết thúc không được trước ngày bắt đầu.");
        }
    }

    private VacationResponse toVacationResponse(Vacation entity) {
        VacationResponse response = new VacationResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setDescription(entity.getDescription());
        response.setEmployeeId(entity.getEmployeeId());
        response.setStartDate(entity.getStartDate());
        response.setEndDate(entity.getEndDate());
        response.setVacationType(entity.getVacationType());
        response.setIsPaid(entity.getIsPaid());
        response.setIsRecurring(entity.getIsRecurring());
        return response;
    }

    private WorkCompensationResponse toWorkCompensationResponse(WorkCompensation entity) {
        WorkCompensationResponse response = new WorkCompensationResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setCompensateDate(entity.getCompensateDate());
        response.setDescription(entity.getDescription());
        return response;
    }
}

