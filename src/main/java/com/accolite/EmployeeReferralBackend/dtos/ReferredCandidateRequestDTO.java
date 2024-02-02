package com.accolite.EmployeeReferralBackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferredCandidateRequestDTO {

    private String primarySkill;
    private String candidateName;
    private double experience;
    private long contactNumber;
    private String candidateEmail;
    private boolean willingToRelocate;
    private String preferredLocation;
    private boolean vouch;
    private boolean servingNoticePeriod;
    private int noticePeriodLeft;
    private boolean offerInHand;
    private String token;
    private int noticePeriod;
    private String ProfileSource;
}
