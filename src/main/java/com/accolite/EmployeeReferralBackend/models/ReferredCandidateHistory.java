package com.accolite.EmployeeReferralBackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString(exclude = {"referredCandidate"})
@EqualsAndHashCode(exclude = {"referredCandidate"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ReferredCandidateHistory")
public class ReferredCandidateHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String interviewStatus;

    @ManyToOne
    @JoinColumn(name = "referred_candidate_id")
    @JsonBackReference
    private ReferredCandidate referredCandidate;

    private LocalDateTime updateDate;

}
