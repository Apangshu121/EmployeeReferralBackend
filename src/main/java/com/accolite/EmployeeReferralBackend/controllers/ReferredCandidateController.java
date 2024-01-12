package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.service.ReferredCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/referredCandidates")
public class ReferredCandidateController {

    @Autowired
    ReferredCandidateService referredCandidateService;


    @PostMapping("/add")
    public ResponseEntity<String> addReferredCandidate(@RequestBody ReferredCandidate referredCandidate) {
        referredCandidateService.addReferredCandidate(referredCandidate);
        return ResponseEntity.ok("Referred candidate added successfully");
    }

    // Modify
    @GetMapping("/getAllCandidatesOfUser")
    public ResponseEntity<Map<String,Object>> getReferredCandidatesOfUser(@RequestBody String googleToken)
    {
        return referredCandidateService.getReferredCandidatesOfUser(googleToken);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Map<String,Object>> getAllCandidates(){
        return referredCandidateService.getAllCandidates();
    }
}
