package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.service.ReferredCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/referredCandidates")
public class ReferredCandidateController {

    private final ReferredCandidateService referredCandidateService;

    @Autowired
    public ReferredCandidateController(ReferredCandidateService referredCandidateService) {
        this.referredCandidateService = referredCandidateService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addReferredCandidate(@RequestBody ReferredCandidate referredCandidate) {
        referredCandidateService.addReferredCandidate(referredCandidate);
        return ResponseEntity.ok("Referred candidate added successfully");
    }
}
