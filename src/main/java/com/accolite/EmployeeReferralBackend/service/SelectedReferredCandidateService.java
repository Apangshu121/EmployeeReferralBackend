package com.accolite.EmployeeReferralBackend.service;

import com.accolite.EmployeeReferralBackend.models.SelectedReferredCandidate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface SelectedReferredCandidateService {

    ResponseEntity<Map<String, Object>> getAllSelectedReferredCandidates();

    ResponseEntity<Map<String, Object>> getSelectedReferredCandidateById(Long id);

    ResponseEntity<Map<String, Object>> updateReferredSelectedCandidatesById(Long id, SelectedReferredCandidate selectedReferredCandidate);

    ResponseEntity<Map<String,Object>> allocateBonusToUser(Long candidateId);
}
