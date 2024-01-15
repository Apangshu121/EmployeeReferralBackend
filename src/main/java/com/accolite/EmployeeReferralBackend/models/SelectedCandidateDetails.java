package com.accolite.EmployeeReferralBackend.models;

import java.time.LocalDateTime;

public class SelectedCandidateDetails {

    private final String name;

    private final LocalDateTime dateOfJoining;

    private final String interviewedRole;


    public SelectedCandidateDetails(String name, LocalDateTime dateOfJoining, String interviewedRole) {
        this.name = name;
        this.dateOfJoining = dateOfJoining;
        this.interviewedRole = interviewedRole;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDateOfJoining() {
        return dateOfJoining;
    }

    public String getInterviewedRole() {
        return interviewedRole;
    }
}
