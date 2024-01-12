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
    public ResponseEntity<Map<String, Object>> getDetailsOfUser(@RequestHeader("Authorization") String authorizationHeader) {

        String googleToken = extractTokenFromHeader(authorizationHeader);
        return userService.getDetailsOfUser(googleToken);
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        String[] headerParts = authorizationHeader.split(" ");
        if (headerParts.length == 2 && "Bearer".equals(headerParts[0])) {
            return headerParts[1];
        }

        return null;
    }
}
