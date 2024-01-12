package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/saveUser")
    public ResponseEntity<Map<String, Object>> saveUser(@RequestBody String googleToken) {
        return authService.saveUser(googleToken);
    }


}
