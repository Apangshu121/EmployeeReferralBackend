package com.accolite.EmployeeReferralBackend.dtos;

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
    private String businessUnit;
    private int totalReferrals;
    private int select;
    private int reject;
    private int inProgress;
    private int todo;
}
