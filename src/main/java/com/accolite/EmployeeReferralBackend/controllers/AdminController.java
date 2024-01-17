package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.models.User;
import com.accolite.EmployeeReferralBackend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/users")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PutMapping("/modify/{userEmail}")
    public ResponseEntity<Map<String,Object>> modifyOrCreateUser(@PathVariable String userEmail, @RequestBody User modifiedUser) {
        return adminService.modifyUser(userEmail, modifiedUser);

    }
}