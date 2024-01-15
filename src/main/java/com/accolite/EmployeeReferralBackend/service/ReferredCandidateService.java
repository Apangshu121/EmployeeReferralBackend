package com.accolite.EmployeeReferralBackend.service;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ReferredCandidateService {

    ResponseEntity<Map<String, Object>> addReferredCandidate(ReferredCandidate referredCandidate);
    ResponseEntity<Map<String,Object>> getReferredCandidatesOfUser();

    ResponseEntity<Map<String, Object>> getAllCandidates();

    ResponseEntity<Map<String, Object>> getCandidateById(int id);

    ResponseEntity<Map<String, Object>> updateReferredCandidate(int id, ReferredCandidate referredCandidate);
}

