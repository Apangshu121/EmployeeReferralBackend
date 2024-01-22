package com.accolite.EmployeeReferralBackend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReferredCandidateRequestDTO {

    private String interviewedPosition;
    private String currentStatus;
    private String interviewStatus;
    private String businessUnit;
    private String band;
}
