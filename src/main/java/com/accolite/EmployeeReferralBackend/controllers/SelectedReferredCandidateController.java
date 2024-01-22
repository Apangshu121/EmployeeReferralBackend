package com.accolite.EmployeeReferralBackend.controllers;


import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.models.SelectedReferredCandidate;
import com.accolite.EmployeeReferralBackend.service.SelectedReferredCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/selectedReferredCandidates")
public class SelectedReferredCandidateController {

    @Autowired
    SelectedReferredCandidateService selectedReferredCandidateService;

    @GetMapping("/getAll")
    public ResponseEntity<Map<String,Object>> getAllSelectedReferredCandidates()
    {
        return selectedReferredCandidateService.getAllSelectedReferredCandidates();
    }

//    @GetMapping("/get/{id}")
//    public ResponseEntity<Map<String,Object>> getSelectedReferredCandidateById(@PathVariable Long id){ return selectedReferredCandidateService.getSelectedReferredCandidateById(id);}

    @PostMapping("/allocateBonus/{candidateId}")
    public ResponseEntity<Map<String,Object>> allocateBonusToUser(@PathVariable Long candidateId) {
        return selectedReferredCandidateService.allocateBonusToUser(candidateId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String,Object>> updateReferredSelectedCandidatesById(@PathVariable Long id, @RequestBody SelectedReferredCandidate selectedReferredCandidate){ return selectedReferredCandidateService.updateReferredSelectedCandidatesById(id, selectedReferredCandidate);}
}
