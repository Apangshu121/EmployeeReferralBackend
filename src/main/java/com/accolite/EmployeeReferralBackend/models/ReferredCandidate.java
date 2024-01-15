package com.accolite.EmployeeReferralBackend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ReferredCandidates")
public class ReferredCandidate{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    int id;
    LocalDateTime dateOfReferral;
    String referrerEmail;
    String primarySkill;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "secondarySkills", joinColumns = @JoinColumn(name = "referral_candidate_id"))
    @Column(name = "secondarySkill", nullable = false)
    @Builder.Default
    Set<String> secondarySkills = new HashSet<>();

    String candidateName;
    int experience;
    long contactNumber;
    String candidateEmail;
    String currentStatus; // Select, Reject, Drop, On Hold, Better qualified for other position, Pool(Default)
    String panNumber;
    boolean willingToRelocate;
    String interviewStatus; // Codelyser Select, R1 Select, R2 Select, R3 Select, Codelyser Reject, R1 Reject, R2 Reject, R3 Reject
    String interviewedPosition;
    String preferredLocation;
    String businessUnit;
    int noticePeriod; // Immediate(0), 15, 30, 45, 60, 90
    String band;

    // Editable by Recruiter:- currentStatus, interviewStatus, interviewedPosition, businessUnit, band
}