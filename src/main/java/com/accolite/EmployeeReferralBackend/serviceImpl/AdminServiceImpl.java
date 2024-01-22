// AdminServiceImpl.java
package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.models.UserDTO;
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
import java.util.stream.Collectors;

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

            List<UserDTO> userDTOs = users.stream().filter((User::isActive))
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            Map<String, Object> responseJson = new HashMap<>();
            responseJson.put("Users", userDTOs);

            return ResponseEntity.ok(responseJson);
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> modifyUser(String email, User modifiedUser)
    {
        try {
           User existingUser = userRepository.findByEmail(email).orElseThrow();



                existingUser.setRole(modifiedUser.getRole());
                // Update other properties as needed

                User savedUser = userRepository.save(existingUser);

                Map<String, Object> responseJson = new HashMap<>();
                responseJson.put("User", convertToDTO(savedUser));

                return ResponseEntity.ok(responseJson);

        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }


    }
    @Override
    public ResponseEntity<Map<String, Object>> deleteUser(long id){
        try {
           User existingUser = userRepository.findById(id).orElseThrow();



                existingUser.setActive(false);
                // Update other properties as needed

                User savedUser = userRepository.save(existingUser);

                Map<String, Object> responseJson = new HashMap<>();
                responseJson.put("Message", "User deleted");

                return ResponseEntity.ok(responseJson);



        } catch (Exception e)
        {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }


    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name(),
                user.getTotalBonus()
        );
    }


}
