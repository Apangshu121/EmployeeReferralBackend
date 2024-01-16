package com.accolite.EmployeeReferralBackend.service;

import com.accolite.EmployeeReferralBackend.models.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface AdminService {

    ResponseEntity<Map<String, Object>> getAllUsers();


    ResponseEntity<Map<String, Object>> modifyUser(String email, User modifiedUser);


}
