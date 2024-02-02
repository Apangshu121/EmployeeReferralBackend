package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.service.SeniorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/senior")
public class SeniorController {

    @Autowired
    SeniorService seniorService;

    @GetMapping("/getCandidatesOfBusinessUnit")
    public ResponseEntity<Map<String, Object>> getCandidatesOfBusinessUnit() {
        return seniorService.getReferredCandidatesOfBusinessUnit();
    }
}
