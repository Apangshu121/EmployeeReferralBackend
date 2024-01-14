package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/getUserDetails")
    public ResponseEntity<Map<String, Object>> getDetailsOfUser() {
        return userService.getDetailsOfUser();
    }
}
