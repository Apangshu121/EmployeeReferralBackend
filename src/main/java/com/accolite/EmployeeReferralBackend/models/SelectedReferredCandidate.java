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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    private long phoneNumber;

    private String interviewedRole;

    private String referrerEmail;

    private LocalDate dateOfJoining;

    private double bonus;

    private boolean currentlyInCompany;

    private boolean bonusAllocated;


    // updated by dateOfJoining, bonusAllocated, currentlyInCompany.
}
