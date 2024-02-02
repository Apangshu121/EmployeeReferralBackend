package com.accolite.EmployeeReferralBackend.service;

import com.accolite.EmployeeReferralBackend.dtos.AdminUpdateDTO;
import com.accolite.EmployeeReferralBackend.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AdminService {

   ResponseEntity<Map<String, Object>> getAllUsers();

  ResponseEntity<Map<String, Object>> modifyUser(String email, User modifiedUser);

   ResponseEntity<Map<String, Object>> deleteUser(long id);

    ResponseEntity<Map<String, Object>> searchUsers(String keyword);
   ResponseEntity<Map<String, Object>> updateReferredCandidate(int id, AdminUpdateDTO updatedCandidateDTO);

}
