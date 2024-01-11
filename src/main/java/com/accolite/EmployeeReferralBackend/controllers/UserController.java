package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/getName")
    public ResponseEntity<Map<String,Object>> getNameOfUser(@RequestBody String googleToken)
    {
        return userService.getNameOfUser(googleToken);
    }
}
