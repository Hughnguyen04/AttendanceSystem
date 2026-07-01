package com.example.attendance.service;

import java.time.Year;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.attendance.dto.AbsenceTrackerResponse;
import com.example.attendance.entity.AbsenceTracker;
import com.example.attendance.repository.AbsenceTrackerRepository;

@Service
public class AbsenceService {
    private final AbsenceTrackerRepository absenceTrackerRepository;

    public AbsenceService(AbsenceTrackerRepository absenceTrackerRepository) {
        this.absenceTrackerRepository = absenceTrackerRepository;
    }

    public AbsenceTrackerResponse getTracker(Long employeeId) {
        Optional<AbsenceTracker> trackerOpt = absenceTrackerRepository.findByEmployeeId(employeeId);
        AbsenceTracker tracker = trackerOpt.orElseGet(() -> {
            AbsenceTracker newTracker = new AbsenceTracker();
            newTracker.setEmployeeId(employeeId);
            newTracker.setCurrentYearTotal(14);
            newTracker.setCurrentYearUsed(0);
            newTracker.setCarriedOverFromLastYear(0);
            newTracker.setCarriedOverUsed(0);
            newTracker.setLastResetYear(Year.now().getValue());
            return absenceTrackerRepository.save(newTracker);
        });

        AbsenceTrackerResponse response = new AbsenceTrackerResponse();
        int remainingLastYear = Math.max(0, tracker.getCarriedOverFromLastYear() - tracker.getCarriedOverUsed());
        int remainingCurrentYear = Math.max(0, tracker.getCurrentYearTotal() - tracker.getCurrentYearUsed());
        response.setTotalRemaining(remainingLastYear + remainingCurrentYear);
        response.setCarriedOverUsed(tracker.getCarriedOverUsed());
        response.setCurrentYearUsed(tracker.getCurrentYearUsed());
        return response;
    }
}
