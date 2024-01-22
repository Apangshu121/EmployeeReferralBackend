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
@ToString(exclude = {"user", "interviewStatus", "selectedReferredCandidate"})
@EqualsAndHashCode(exclude = {"user", "interviewStatus", "selectedReferredCandidate"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "referred_candidates")
public class ReferredCandidate{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Y

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    private String primarySkill; // Y
    private int noOfTimesReferred;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "secondarySkills", joinColumns = @JoinColumn(name = "referral_candidate_id"))
    @Column(name = "secondarySkill", nullable = false)
    private Set<String> secondarySkills = new HashSet<>(); // N

    private String candidateName; // Y
    private double experience; // Y
    private long contactNumber; // Y
    private String candidateEmail; // Y

    private String panNumber; // Y
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

    @OneToMany(mappedBy = "referredCandidate", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ReferredCandidateHistory> referredCandidateHistory; // N

    private boolean isActive; // Y

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "selectedCandidate", referencedColumnName = "id")
    private SelectedReferredCandidate selectedReferredCandidate;

    private LocalDateTime updatedAt;

    // Editable by Recruiter:- interviewedPosition, businessUnit, band
}