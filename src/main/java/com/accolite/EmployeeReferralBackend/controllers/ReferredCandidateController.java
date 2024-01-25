package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.models.ReferredCandidateRequestDTO;
import com.accolite.EmployeeReferralBackend.models.UpdateReferredCandidateRequestDTO;
import com.accolite.EmployeeReferralBackend.service.ReferredCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
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
    public ResponseEntity<Map<String,Object>> getReferredCandidatesOfUser(@RequestParam(value = "token", required = false) String token)
    {
        return referredCandidateService.getReferredCandidatesOfUser(token);
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

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String,Object>> updateReferredCandidate(@PathVariable int id, @RequestBody UpdateReferredCandidateRequestDTO referredCandidate){
        return referredCandidateService.updateReferredCandidate(id, referredCandidate);
    }

    @GetMapping("/filterByExperience/{experience}")
    public ResponseEntity<Map<String,Object>> filterCandidatesByExperience(
            @PathVariable int experience, @RequestParam(value = "keyword", required = false) String keyword) {
        if (keyword == null) {
            // If a search keyword is provided, return the products that match the name
            return referredCandidateService.filterCandidatesByExperience(experience);
        } else {
            // If no search keyword, return all products
            return referredCandidateService.filterCandidatesByExperienceAndSearch(experience, keyword);
        }
    }
    @GetMapping("/filterByPreferredLocation/{preferredLocation}")
    public ResponseEntity<Map<String,Object>> filterCandidatesByPreferredLocation(
            @PathVariable String preferredLocation, @RequestParam(value = "keyword", required = false) String keyword) {

        if (keyword == null) {
            // If a search keyword is provided, return the products that match the name
            return referredCandidateService.filterCandidatesByPreferredLocation(preferredLocation);
        } else {
            // If no search keyword, return all products
            return referredCandidateService.filterCandidatesByPreferredLocationAndSearch(preferredLocation, keyword);
        }
    }

    @GetMapping("/filterByNoticePeriod/{noticePeriod}")
    public ResponseEntity<Map<String,Object>> filterCandidatesByNoticePeriod(
            @PathVariable int noticePeriod, @RequestParam(value = "keyword", required = false) String keyword) {

        if (keyword == null) {
            // If a search keyword is provided, return the products that match the name
            return referredCandidateService.filterCandidatesByNoticePeriodLessThanOrEqual(noticePeriod);
        } else {
            // If no search keyword, return all products
            return referredCandidateService.filterCandidatesByNoticePeriodLessThanOrEqualAndSearch(noticePeriod, keyword);
        }
    }

    @PostMapping("/sendMail/{id}")
    public ResponseEntity<Map<String,Object>> sendMail(@PathVariable int id){
        return referredCandidateService.sendMail(id);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downloadResume(@PathVariable int id) {
        return referredCandidateService.downloadResume(id);
    }
    @GetMapping("/by-interview-status/{status}")
    public ResponseEntity<Map<String, Object>> getReferredCandidatesByInterviewStatus(@PathVariable String status) {
        return referredCandidateService.getReferredCandidatesByInterviewStatus(status);
    }
}
