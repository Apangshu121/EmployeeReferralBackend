package com.accolite.EmployeeReferralBackend.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleTokenPayload {
    String email;
    String name;
}
