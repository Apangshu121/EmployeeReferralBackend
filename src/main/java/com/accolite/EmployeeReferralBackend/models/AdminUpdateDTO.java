package com.accolite.EmployeeReferralBackend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateDTO {
    private String primarySkill;
    private String candidateName; // Y
    private double experience; // Y
    private long contactNumber; // Y
    private String candidateEmail; // Y


    private boolean willingToRelocate; // Y

    private String preferredLocation; // Y
    private boolean servingNoticePeriod; // Y
    private int noticePeriodLeft; // Y
    private boolean offerInHand; // Y
}
