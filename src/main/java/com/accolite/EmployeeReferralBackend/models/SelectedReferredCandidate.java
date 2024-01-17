package com.accolite.EmployeeReferralBackend.models;

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
public class SelectedReferredCandidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    LocalDate dateOfSelection;

    LocalDate dateOfJoining;

    String interviewedRole;

    double bonus;

    String referrerEmail;

    boolean currentlyInCompany;

    boolean bonusAllocated;

    @OneToOne
    @JoinColumn(name = "referred_candidate_id", unique = true)
    private ReferredCandidate referredCandidate;
    // updated by dateOfJoining, bonusAllocated, currentlyInCompany.
}
