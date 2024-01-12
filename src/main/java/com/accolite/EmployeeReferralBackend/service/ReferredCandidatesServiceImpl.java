package com.accolite.EmployeeReferralBackend.service;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.repository.ReferredCandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ReferredCandidatesServiceImpl implements ReferredCandidateService{
    @Autowired
    ReferredCandidateRepository referredCandidateRepository;
    @Override
    public List<ReferredCandidate> getAll() {
        return referredCandidateRepository.findAll();
    }
}
