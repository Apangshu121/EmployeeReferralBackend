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
    public ResponseEntity<String> addReferredCandidate(@RequestHeader("Authorization") String authorizationHeader,@RequestBody ReferredCandidate referredCandidate) {
        String googleToken = extractTokenFromHeader(authorizationHeader);
        referredCandidateService.addReferredCandidate(googleToken, referredCandidate);

        return ResponseEntity.ok("Referred candidate added successfully");
    }

    // Modify
    @GetMapping("/getAllCandidatesOfUser")
    public ResponseEntity<Map<String,Object>> getReferredCandidatesOfUser(@RequestHeader("Authorization") String authorizationHeader)
    {
        String googleToken = extractTokenFromHeader(authorizationHeader);
        return referredCandidateService.getReferredCandidatesOfUser(googleToken);
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        String[] headerParts = authorizationHeader.split(" ");
        if (headerParts.length == 2 && "Bearer".equals(headerParts[0])) {
            return headerParts[1];
        }

        return null;
    }

    @GetMapping("/getAll")
    public ResponseEntity<Map<String,Object>> getAllCandidates(){
        return referredCandidateService.getAllCandidates();
    }
}
