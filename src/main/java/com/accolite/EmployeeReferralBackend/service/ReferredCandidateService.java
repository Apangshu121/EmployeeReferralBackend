package com.accolite.EmployeeReferralBackend.service;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.models.UpdateReferredCandidateRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ReferredCandidateService {

    ResponseEntity<Map<String, Object>> addReferredCandidate(ReferredCandidate referredCandidate);
    ResponseEntity<Map<String,Object>> getReferredCandidatesOfUser();

    ResponseEntity<Map<String, Object>> getAllCandidates();

    //  ResponseEntity<Map<String, Object>> getCandidateById(int id);

    ResponseEntity<Map<String, Object>> updateReferredCandidate(int id, UpdateReferredCandidateRequestDTO referredCandidate);

    ResponseEntity<Map<String, Object>> interviewTheCandidate(int id);

    ResponseEntity<Map<String,Object>> filterCandidatesByExperience(int experience);

    ResponseEntity<Map<String,Object>> filterCandidatesByPreferredLocation(String preferredLocation);

    ResponseEntity<Map<String,Object>> filterCandidatesByNoticePeriodLessThanOrEqual(int noticePeriod);

    ResponseEntity<Map<String,Object>> sendMail(int id);

    ResponseEntity<Map<String, Object>> searchCandidates(String keyword);
}

