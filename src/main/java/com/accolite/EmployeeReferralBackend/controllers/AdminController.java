//package com.accolite.EmployeeReferralBackend.controllers;
//
//import com.accolite.EmployeeReferralBackend.models.AdminUpdateDTO;
//import com.accolite.EmployeeReferralBackend.models.User;
//import com.accolite.EmployeeReferralBackend.service.AdminService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/admin/users")
//public class AdminController {
//
//    private final AdminService adminService;
//
//    @Autowired
//    public AdminController(AdminService adminService) {
//        this.adminService = adminService;
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<Map<String, Object>> getAllUsers(@RequestParam(value = "keyword", required = false) String keyword) {
//        if (keyword != null && !keyword.isEmpty()) {
//            // If a search keyword is provided, return the products that match the name
//            return adminService.searchUsers(keyword);
//        } else {
//            // If no search keyword, return all products
//            return adminService.getAllUsers();
//        }
//
//    }
//
//    @PutMapping("/modify/{userEmail}")
//    public ResponseEntity<Map<String, Object>> modifyOrCreateUser(@PathVariable String userEmail, @RequestBody User modifiedUser) {
//        return adminService.modifyUser(userEmail, modifiedUser);
//
//
//    }
//    @PutMapping("/editReferredCandidate/{id}")
//    public ResponseEntity<Map<String, Object>> editReferredCandidate(@PathVariable int id, @RequestBody AdminUpdateDTO adminUpdateDTO) {
//        // Call the service method to update the referred candidate
//        return adminService.updateReferredCandidate(id, adminUpdateDTO);
//    }
//    @PutMapping("/delete/{id}")
//    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable long id) {
//        return adminService.deleteUser(id);
//    }
//}