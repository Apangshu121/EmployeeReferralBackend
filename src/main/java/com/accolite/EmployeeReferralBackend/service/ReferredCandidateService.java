package com.accolite.EmployeeReferralBackend.service;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.repository.ReferredCandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public interface ReferredCandidateService {

    void addReferredCandidate(ReferredCandidate referredCandidate);
    ResponseEntity<Map<String,Object>> getReferredCandidatesOfUser(String googleToken);

    ResponseEntity<Map<String, Object>> getAllCandidates();
}

