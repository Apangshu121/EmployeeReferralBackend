package com.accolite.EmployeeReferralBackend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    String candidateName;
    int experience;
    long contactNumber;
    String candidateEmail;
    String currentStatus;
    String panNumber;
    boolean willingToRelocate;
    String interviewStatus;
    //Band
    String interviewedPosition;
    String currentLocation;
    String businessUnit;
}