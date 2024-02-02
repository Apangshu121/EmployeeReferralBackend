package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.dtos.StatusTalyDTO;
import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.dtos.UpdateReferredCandidateRequestDTO;
import com.accolite.EmployeeReferralBackend.service.ReferredCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @GetMapping("/filter/{condition}/{filterValue}")
    public ResponseEntity<Map<String,Object>> filterCandidatesByExperience(
            @PathVariable String condition, @PathVariable String filterValue, @RequestParam(value = "keyword", required = false) String keyword) {

        if(condition.equalsIgnoreCase("experience"))
        {
            if (keyword == null) {
                // If a search keyword is provided, return the products that match the name
                return referredCandidateService.filterCandidatesByExperience(Integer.parseInt(filterValue));
            } else {
                // If no search keyword, return all products
                return referredCandidateService.filterCandidatesByExperienceAndSearch(Integer.parseInt(filterValue), keyword);
            }
        }else if(condition.equalsIgnoreCase("preferredLocation"))
        {
            if (keyword == null) {
                // If a search keyword is provided, return the products that match the name
                return referredCandidateService.filterCandidatesByPreferredLocation(filterValue);
            } else {
                // If no search keyword, return all products
                return referredCandidateService.filterCandidatesByPreferredLocationAndSearch(filterValue, keyword);
            }
        }else if(condition.equalsIgnoreCase("noticePeriod"))
        {
            if (keyword == null) {
                // If a search keyword is provided, return the products that match the name
                return referredCandidateService.filterCandidatesByNoticePeriodLessThanOrEqual(Integer.parseInt(filterValue));
            } else {
                // If no search keyword, return all products
                return referredCandidateService.filterCandidatesByNoticePeriodLessThanOrEqualAndSearch(Integer.parseInt(filterValue), keyword);
            }
        }else if(condition.equalsIgnoreCase("interviewStatus"))
        {
            if(keyword == null)
            {
                return referredCandidateService.getReferredCandidatesByInterviewStatus(filterValue);
            }else{
                return referredCandidateService.getReferredCandidatesByInterviewStatusAndSearch(filterValue, keyword);
            }
        }else{
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
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

    @GetMapping("/statusTally")
    public ResponseEntity<StatusTalyDTO> getStatusTally() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = ((UserDetails) principal).getUsername();
            StatusTalyDTO statusTallyDTO = referredCandidateService.getStatusTallyForUser(email);
            return ResponseEntity.ok(statusTallyDTO);
        } catch (Exception e) {
            // Handle exceptions as needed
            return ResponseEntity.status(500).build();
        }
    }

}
