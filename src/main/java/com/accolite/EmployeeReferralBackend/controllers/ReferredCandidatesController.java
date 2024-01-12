package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.service.ReferredCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class ReferredCandidatesController{

    @Autowired
    ReferredCandidateService referredCandidateService;

    @GetMapping("/getAllReferrals")
    public List<ReferredCandidate> getAllCandidates(){
        return referredCandidateService.getAll();
    }
}
