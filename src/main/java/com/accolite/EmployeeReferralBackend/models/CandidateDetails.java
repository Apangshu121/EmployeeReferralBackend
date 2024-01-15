package com.accolite.EmployeeReferralBackend.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CandidateDetails {

    private final String candidateName;
    private final String primarySkill;
    private final String interviewStatus;
    private final String interviewedPosition;

    private final Set<String> secondarySkills;

    public CandidateDetails(String candidateName, String primarySkill, String interviewStatus, String interviewedPosition, String secondarySkill1, String secondarySkill2) {
        this.candidateName = candidateName;
        this.primarySkill = primarySkill;
        this.interviewStatus = interviewStatus;
        this.interviewedPosition = interviewedPosition;
        this.secondarySkills = new HashSet<>(Arrays.asList(secondarySkill1, secondarySkill2));
    }


    public Set<String> getSecondarySkills() {
        return secondarySkills;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public String getPrimarySkill() {
        return primarySkill;
    }

    public String getInterviewStatus() {
        return interviewStatus;
    }

    public String getInterviewedPosition() {
        return interviewedPosition;
    }
}
