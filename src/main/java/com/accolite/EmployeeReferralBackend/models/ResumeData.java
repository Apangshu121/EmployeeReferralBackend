package com.accolite.EmployeeReferralBackend.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResumeData {

    private String name;
    private String email;
    private String phone;
    private String experience;
    private String primarySkill;
    private String filename;
    private boolean isBlacklisted;
}
