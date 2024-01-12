package com.accolite.EmployeeReferralBackend.service;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ReferredCandidateService {

    void addReferredCandidate(String googleToken, ReferredCandidate referredCandidate);
    ResponseEntity<Map<String,Object>> getReferredCandidatesOfUser(String googleToken);

    ResponseEntity<Map<String, Object>> getAllCandidates();
}

