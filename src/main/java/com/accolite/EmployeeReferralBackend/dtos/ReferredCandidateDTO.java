package com.accolite.EmployeeReferralBackend.dtos;

import com.accolite.EmployeeReferralBackend.models.InterviewStatus;
import com.accolite.EmployeeReferralBackend.models.ReferredCandidateHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferredCandidateDTO {

    private Long id;
    private String primarySkill;
    private String candidateName;
    private double experience;
    private long contactNumber;
    private String candidateEmail;
    private String referrerEmail;
    private boolean willingToRelocate;
    private String interviewedPosition;
    private boolean interviewTheCandidate;
    private String preferredLocation;
    private String businessUnit;
    private String band;
    private boolean vouch;
    private boolean servingNoticePeriod;
    private int noticePeriodLeft;
    private boolean offerInHand;
    private InterviewStatus interviewStatus;
    private Set<ReferredCandidateHistory> referredCandidateHistories;
    private boolean blacklisted;
}

