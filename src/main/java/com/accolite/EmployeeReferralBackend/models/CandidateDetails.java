package com.accolite.EmployeeReferralBackend.models;

import java.time.LocalDateTime;

public class CandidateDetails {

    private final String candidateName;
    private final LocalDateTime dateOfReferral;
    private String interviewStatus;
    private String interviewedPosition;

    public CandidateDetails(String candidateName, LocalDateTime dateOfReferral, String interviewStatus, String interviewedPosition) {
        this.candidateName = candidateName;
        this.dateOfReferral = dateOfReferral;
        this.interviewStatus = interviewStatus;
        this.interviewedPosition = interviewedPosition;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public LocalDateTime getDateOfReferral() {
        return dateOfReferral;
    }

    public String getInterviewStatus() {
        return interviewStatus;
    }

    public String getInterviewedPosition() {
        return interviewedPosition;
    }
}
