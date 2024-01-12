package com.accolite.EmployeeReferralBackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "ReferredCandidates")
public class ReferredCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    int id;
    Date dateOfReferral;
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
