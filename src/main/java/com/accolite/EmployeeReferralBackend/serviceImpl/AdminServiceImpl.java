// AdminServiceImpl.java
package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.dtos.AdminUpdateDTO;
import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.models.User;
import com.accolite.EmployeeReferralBackend.dtos.UserDTO;
import com.accolite.EmployeeReferralBackend.repository.ReferredCandidateRepository;
import com.accolite.EmployeeReferralBackend.repository.UserRepository;
import com.accolite.EmployeeReferralBackend.service.AdminService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Autowired
    private ReferredCandidateRepository referredCandidateRepository;

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

                if(existingUser.getRole()!=null)
                    existingUser.setRole(modifiedUser.getRole());

                // Update other properties as needed

                User savedUser = userRepository.save(existingUser);

                Map<String, Object> responseJson = new HashMap<>();
                responseJson.put("User", convertToDTO(savedUser));

                return ResponseEntity.ok(responseJson);

        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public ResponseEntity<Map<String, Object>> searchUsers(String keyword) {
        try{
            Specification<User> specification = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (keyword != null && !keyword.isEmpty()) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

            List<User> searchedUsersList = userRepository.findAll(specification);
            List<UserDTO> userDTOS = searchedUsersList.stream()
                    .map(this::convertToDTO)
                    .toList();

            Map<String, Object> responseJson = new HashMap<>();

            responseJson.put("Searched Candidates", userDTOS);

            return ResponseEntity.ok(responseJson);
        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

@Override
    public ResponseEntity<Map<String, Object>> updateReferredCandidate(int id, AdminUpdateDTO updatedCandidateDTO)
    {
try {
    ReferredCandidate existingCandidate = referredCandidateRepository.findById(id).orElseThrow();


    // Update the editable fields

    if (updatedCandidateDTO.getPrimarySkill() != null) {
        existingCandidate.setPrimarySkill(updatedCandidateDTO.getPrimarySkill());
    }

    if (updatedCandidateDTO.getCandidateName() != null) {
        existingCandidate.setCandidateName(updatedCandidateDTO.getCandidateName());
    }

    if (updatedCandidateDTO.getExperience() != 0) {
        existingCandidate.setExperience(updatedCandidateDTO.getExperience());
    }

    if (updatedCandidateDTO.getContactNumber() != 0) {
        existingCandidate.setContactNumber(updatedCandidateDTO.getContactNumber());
    }

    if (updatedCandidateDTO.getCandidateEmail() != null) {
        existingCandidate.setCandidateEmail(updatedCandidateDTO.getCandidateEmail());
    }


        existingCandidate.setWillingToRelocate(updatedCandidateDTO.isWillingToRelocate());


    if (updatedCandidateDTO.getPreferredLocation() != null) {
        existingCandidate.setPreferredLocation(updatedCandidateDTO.getPreferredLocation());
    }


        existingCandidate.setServingNoticePeriod(updatedCandidateDTO.isServingNoticePeriod());


    if (updatedCandidateDTO.getNoticePeriodLeft() != 0) {
        existingCandidate.setNoticePeriodLeft(updatedCandidateDTO.getNoticePeriodLeft());
    }


        existingCandidate.setOfferInHand(updatedCandidateDTO.isOfferInHand());




    // Save the updated candidate
    ReferredCandidate referredCandidate = referredCandidateRepository.save(existingCandidate);
    AdminUpdateDTO adminUpdateDTO= convertToAdminDTO(referredCandidate);
    Map<String, Object> responseJson = new HashMap<>();
    responseJson.put("User", adminUpdateDTO);

    return ResponseEntity.ok(responseJson);

}catch (Exception e) {
    Map<String, Object> errorMap = new HashMap<>();
    errorMap.put("status", "error");
    errorMap.put("message", "An error occurred");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
}

    }

    private AdminUpdateDTO convertToAdminDTO(ReferredCandidate referredCandidate) {
        AdminUpdateDTO adminUpdateDTO = new AdminUpdateDTO();
        // Set fields in AdminUpdateDTO based on referredCandidate
        adminUpdateDTO.setPrimarySkill(referredCandidate.getPrimarySkill());
        adminUpdateDTO.setCandidateName(referredCandidate.getCandidateName());
        adminUpdateDTO.setExperience(referredCandidate.getExperience());
        adminUpdateDTO.setContactNumber(referredCandidate.getContactNumber());
        adminUpdateDTO.setCandidateEmail(referredCandidate.getCandidateEmail());
        adminUpdateDTO.setWillingToRelocate(referredCandidate.isWillingToRelocate());
        adminUpdateDTO.setPreferredLocation(referredCandidate.getPreferredLocation());
        adminUpdateDTO.setServingNoticePeriod(referredCandidate.isServingNoticePeriod());
        adminUpdateDTO.setNoticePeriodLeft(referredCandidate.getNoticePeriodLeft());
        adminUpdateDTO.setOfferInHand(referredCandidate.isOfferInHand());
        // Set other fields...

        return adminUpdateDTO;
    }
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );
    }
//
//
}
