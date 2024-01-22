package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.models.UpdateReferredCandidateRequestDTO;
import com.accolite.EmployeeReferralBackend.service.ReferredCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/referredCandidates")
public class ReferredCandidateController {

    @Autowired
    ReferredCandidateService referredCandidateService;


    @PostMapping("/add")
    public ResponseEntity<Map<String,Object>> addReferredCandidate(@RequestBody ReferredCandidate referredCandidate) {
        return referredCandidateService.addReferredCandidate(referredCandidate);

    }

    @GetMapping("/getAllCandidatesOfUser")
    public ResponseEntity<Map<String,Object>> getReferredCandidatesOfUser()
    {
        return referredCandidateService.getReferredCandidatesOfUser();
    }

    @GetMapping("/getAll")
    public ResponseEntity<Map<String,Object>> getAllCandidates(@RequestParam(value = "keyword", required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            // If a search keyword is provided, return the products that match the name
            return referredCandidateService.searchCandidates(keyword);
        } else {
            // If no search keyword, return all products
            return referredCandidateService.getAllCandidates();
        }
    }

    @PutMapping("/selectReferredCandidateForInterview/{id}")
    public ResponseEntity<Map<String,Object>> interviewTheCandidate(@PathVariable int id){
        return referredCandidateService.interviewTheCandidate(id);
    }

//    @GetMapping("/get/{id}")
//    public ResponseEntity<Map<String,Object>> getReferredCandidateById(@PathVariable int id){ return referredCandidateService.getCandidateById(id);}

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String,Object>> updateReferredCandidate(@PathVariable int id, @RequestBody UpdateReferredCandidateRequestDTO referredCandidate){ return referredCandidateService.updateReferredCandidate(id, referredCandidate);}

    @GetMapping("/filterByExperience/{experience}")
    public ResponseEntity<Map<String,Object>> filterCandidatesByExperience(
            @PathVariable int experience) {
        return referredCandidateService.filterCandidatesByExperience(experience);
    }
    @GetMapping("/filterByPreferredLocation/{preferredLocation}")
    public ResponseEntity<Map<String,Object>> filterCandidatesByPreferredLocation(
            @PathVariable String preferredLocation) {
        return referredCandidateService.filterCandidatesByPreferredLocation(preferredLocation);
    }

    @GetMapping("/filterByNoticePeriod/{noticePeriod}")
    public ResponseEntity<Map<String,Object>> filterCandidatesByNoticePeriod(
            @PathVariable int noticePeriod) {
        return referredCandidateService.filterCandidatesByNoticePeriodLessThanOrEqual(noticePeriod);
    }

    @PostMapping("/sendMail/{id}")
    public ResponseEntity<Map<String,Object>> sendMail(@PathVariable int id){
        return referredCandidateService.sendMail(id);
    }
}
