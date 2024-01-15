package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.models.User;
import com.accolite.EmployeeReferralBackend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/modify/{userId}")
    public ResponseEntity<String> modifyOrCreateUser(@PathVariable Long userId, @RequestBody User modifiedUser) {
        boolean success = adminService.modifyOrCreateUser(userId, modifiedUser);
        if (success) {
            return ResponseEntity.ok("User modified or created successfully");
        } else {
            return ResponseEntity.badRequest().body("User modification or creation failed");
        }
    }


}