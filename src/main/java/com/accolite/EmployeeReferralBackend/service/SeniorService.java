package com.accolite.EmployeeReferralBackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface SeniorService {

    ResponseEntity<Map<String,Object>> getReferredCandidatesOfBusinessUnit();

}
