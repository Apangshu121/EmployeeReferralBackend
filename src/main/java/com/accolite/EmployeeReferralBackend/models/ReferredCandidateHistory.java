package com.accolite.EmployeeReferralBackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ReferredCandidateHistory")
public class ReferredCandidateHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referred_candidate_id")
    @JsonBackReference
    private ReferredCandidate referredCandidate;

    private String interviewStatus;

    private LocalDate updateDate;

    // Getter and Setter for all fields

    // Other methods...
}
