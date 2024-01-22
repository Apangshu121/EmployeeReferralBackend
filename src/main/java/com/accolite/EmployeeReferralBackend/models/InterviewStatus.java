package com.accolite.EmployeeReferralBackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "referred_candidates_interview_status")
public class InterviewStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String currentStatus; // Select, Reject, Drop, On Hold, Better qualified for other position, Pool(Default)
    private boolean currentStatusUpdated; // N
    private String interviewStatus; // Codelyser Select, R1 Select, R2 Select, R3 Select, Codelyser Reject, R1 Reject, R2 Reject, R3 Reject, Pool(Default)
    private boolean interviewStatusUpdated;
}
