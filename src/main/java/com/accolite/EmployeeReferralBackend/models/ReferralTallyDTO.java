package com.accolite.EmployeeReferralBackend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferralTallyDTO {

    private String name;
    private int totalReferrals;
    private int select;
    private int reject;
    private int inProgress;
}
