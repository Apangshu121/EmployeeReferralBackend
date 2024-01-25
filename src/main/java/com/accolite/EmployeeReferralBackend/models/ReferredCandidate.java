package com.accolite.EmployeeReferralBackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "referred_candidates")
@ToString(exclude = {"interviewStatus", "referredCandidateHistory", "resume"})
@EqualsAndHashCode(exclude = {"interviewStatus", "referredCandidateHistory", "resume"})
public class ReferredCandidate{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //

    private String referrerEmail;
    private String primarySkill; // Y

    private String candidateName; // Y
    private double experience; // Y
    private long contactNumber; // Y
    private String candidateEmail; // Y

    private boolean willingToRelocate; // Y
    private String interviewedPosition; // N
    private boolean interviewTheCandidate; // N

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "interview_status", referencedColumnName = "id")
    private InterviewStatus interviewStatus; // N

    private String preferredLocation; // Y
    private String businessUnit; // N
    private int noticePeriod; // Immediate(0), 15, 30, 45, 60, 90 // Y
    private String band; // N
    private String profileSource; // Y
    private boolean vouch; // Y
    private boolean servingNoticePeriod; // Y
    private int noticePeriodLeft; // Y
    private boolean offerInHand; // Y

    @Lob
    @Column(name = "resume", columnDefinition = "BLOB")
    private byte[] resume;

    private String fileName;

    @OneToMany(mappedBy = "referredCandidate", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ReferredCandidateHistory> referredCandidateHistory;

    private boolean isActive; // Y

    private LocalDateTime updatedAt;

    // Editable by Recruiter:- interviewedPosition, businessUnit, band
}