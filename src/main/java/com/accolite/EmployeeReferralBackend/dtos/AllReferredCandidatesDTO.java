package com.accolite.EmployeeReferralBackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllReferredCandidatesDTO {

    private Long id;
    private LocalDate dateOfReferral;
    private String interviewedPosition;
    private String currentStatus;
    private String interviewStatus;
}
