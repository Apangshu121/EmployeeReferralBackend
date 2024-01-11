package com.accolite.EmployeeReferralBackend.service;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.repository.ReferredCandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReferredCandidateService {

    private final ReferredCandidateRepository referredCandidateRepository;

    @Autowired
    public ReferredCandidateService(ReferredCandidateRepository referredCandidateRepository) {
        this.referredCandidateRepository = referredCandidateRepository;
    }

    public void addReferredCandidate(ReferredCandidate referredCandidate) {
        Optional<ReferredCandidate> existingCandidate = referredCandidateRepository.findByPanNumber(referredCandidate.getPanNumber());

        if (existingCandidate.isPresent()) {
            // Pan number already exists, handle the error (throw an exception or handle it according to your needs)
            throw new IllegalStateException("Duplicate PAN number found: " + referredCandidate.getPanNumber());
        }

        // If no duplicacy, save the new referred candidate
        referredCandidateRepository.save(referredCandidate);
    }
    }

