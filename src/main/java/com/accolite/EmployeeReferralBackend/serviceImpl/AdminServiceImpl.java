package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.models.User;
import com.accolite.EmployeeReferralBackend.repository.UserRepository;
import com.accolite.EmployeeReferralBackend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getAllUsers() {

        try {
            List<User> users = userRepository.findAll();

            Map<String,Object> responseJson = new HashMap<>();

            responseJson.put("Users",users);

            return ResponseEntity.ok(responseJson);
        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }

    }


    @Override
    public ResponseEntity<Map<String,Object>> modifyUser(String email, User modifiedUser) {
        try{
            User existingUser = userRepository.findByEmail(email).orElseThrow();

            existingUser.setRole(modifiedUser.getRole());
            // Update other properties as needed

            User savedUser = userRepository.save(existingUser);

            Map<String,Object> responseJson = new HashMap<>();

            responseJson.put("User",savedUser);

            return ResponseEntity.ok(responseJson);

        }catch (Exception e){
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("status", "error");
                errorMap.put("message", "An error occurred");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }

    }
}