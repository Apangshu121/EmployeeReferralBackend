package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.models.SelectedReferredCandidate;
import com.accolite.EmployeeReferralBackend.models.User;
import com.accolite.EmployeeReferralBackend.repository.SelectedReferredCandidateRepository;
import com.accolite.EmployeeReferralBackend.repository.UserRepository;
import com.accolite.EmployeeReferralBackend.service.SelectedReferredCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SelectedReferredCandidateServiceImpl implements SelectedReferredCandidateService {

    @Autowired
    SelectedReferredCandidateRepository selectedReferredCandidateRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Map<String, Object>> getAllSelectedReferredCandidates() {
        try {
            List<SelectedReferredCandidate> selectedReferredCandidateList = selectedReferredCandidateRepository.findAll();

            Map<String, Object> responseJson = new HashMap<>();

            responseJson.put("SelectedReferredCandidates",selectedReferredCandidateList);

            return ResponseEntity.ok(responseJson);
        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

//    @Override
//    public ResponseEntity<Map<String, Object>> getSelectedReferredCandidateById(Long id) {
//        try{
//            Map<String, Object> responseJson = new HashMap<>();
//            Optional<SelectedReferredCandidate> selectedReferredCandidate = selectedReferredCandidateRepository.findById(id);
//
//            selectedReferredCandidate.ifPresent(candidate -> responseJson.put("candidate", candidate));
//
//            return ResponseEntity.ok(responseJson);
//        }catch (Exception e){
//            Map<String, Object> errorMap = new HashMap<>();
//            errorMap.put("status", "error");
//            errorMap.put("message", "An error occurred");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
//        }
//    }

    @Override
    public ResponseEntity<Map<String,Object>> allocateBonusToUser(Long candidateId)
    {
        try {
            SelectedReferredCandidate candidate = selectedReferredCandidateRepository.findById(candidateId).orElseThrow();

            String referrerEmail = candidate.getReferrerEmail();
            User user = userRepository.findByEmail(referrerEmail).orElseThrow();

            if (user != null && !candidate.isBonusAllocated()) {
                double bonusAmount = candidate.getBonus();
                user.setTotalBonus(user.getTotalBonus() + bonusAmount);
                userRepository.save(user);


                candidate.setBonusAllocated(true);
                selectedReferredCandidateRepository.save(candidate);

                Map<String, Object> responseJson = new HashMap<>();

                responseJson.put("Message", "Bonus Allocated");

                return ResponseEntity.ok(responseJson);
            }else{
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("status", "error");
                errorMap.put("message", "User does not exist or bonus already allocated");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
            }

        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> updateReferredSelectedCandidatesById(Long id, SelectedReferredCandidate updatedSelectedReferredCandidate) {
        try {
            SelectedReferredCandidate selectedReferredCandidate = selectedReferredCandidateRepository.findById(id).orElseThrow();
            // updated by dateOfJoining, currentlyInCompany, interviewedRole

            if(updatedSelectedReferredCandidate.getDateOfJoining()!=null){
                selectedReferredCandidate.setDateOfJoining(updatedSelectedReferredCandidate.getDateOfJoining());
            }

            if(updatedSelectedReferredCandidate.getInterviewedRole()!=null){
                selectedReferredCandidate.setInterviewedRole(updatedSelectedReferredCandidate.getInterviewedRole());
            }

            if(!updatedSelectedReferredCandidate.isCurrentlyInCompany()){
                selectedReferredCandidate.setCurrentlyInCompany(false);
            }

            selectedReferredCandidateRepository.save(selectedReferredCandidate);

            Map<String, Object> responseJson = new HashMap<>();
            responseJson.put("status","Successfully Updated the details");

            return ResponseEntity.ok(responseJson);
        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }
}
