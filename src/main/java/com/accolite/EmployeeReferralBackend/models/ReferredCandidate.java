package com.accolite.EmployeeReferralBackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    int id; // Y
    LocalDateTime dateOfReferral; // Y
    String referrerEmail; // Y
    String primarySkill; // Y

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "secondarySkills", joinColumns = @JoinColumn(name = "referral_candidate_id"))
    @Column(name = "secondarySkill", nullable = false)
    @Builder.Default
    Set<String> secondarySkills = new HashSet<>(); // N

    String candidateName; // Y
    double experience; // Y
    long contactNumber; // Y
    String candidateEmail; // Y
    String currentStatus; // Select, Reject, Drop, On Hold, Better qualified for other position, Pool(Default)
    boolean currentStatusUpdated; // N
    String panNumber; // Y
    boolean willingToRelocate; // Y
    String interviewStatus; // Codelyser Select, R1 Select, R2 Select, R3 Select, Codelyser Reject, R1 Reject, R2 Reject, R3 Reject // N
    boolean interviewStatusUpdated; // N
    String interviewedPosition; // N
    String preferredLocation; // Y
    String businessUnit; // N
    int noticePeriod; // Immediate(0), 15, 30, 45, 60, 90 // Y
    String band; // N
    String profileSource; // Y
    boolean vouch; // Y
    boolean servingNoticePeriod; // Y
    int noticePeriodLeft; // Y
    boolean offerInHand; // Y

    @OneToMany(mappedBy = "referredCandidate", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ReferredCandidateHistory> referredCandidateHistory; // N

    // Editable by Recruiter:- currentStatus, interviewStatus, interviewedPosition, businessUnit, band
}