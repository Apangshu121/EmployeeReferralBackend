package com.accolite.EmployeeReferralBackend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    String panNumber;

    LocalDate dateOfSelection;

    LocalDate dateOfJoining;

    String interviewedRole;

    double bonus;

    String referrerEmail;

    boolean currentlyInCompany;

    boolean bonusAllocated;

    // updated by dateOfJoining, bonusAllocated, currentlyInCompany.
}
