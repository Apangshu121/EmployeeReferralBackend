package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.models.GoogleTokenPayload;
import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.repository.ReferredCandidateRepository;
import com.accolite.EmployeeReferralBackend.service.ReferredCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ReferredCandidateServiceImpl implements ReferredCandidateService {

    private final String googleTokenInfoUrl = "https://www.googleapis.com/oauth2/v3/tokeninfo";

    @Autowired
    ReferredCandidateRepository referredCandidateRepository;

    public void addReferredCandidate(String googleToken, ReferredCandidate referredCandidate) {

        RestTemplate restTemplate = new RestTemplate();
        String tokenInfoUrl = googleTokenInfoUrl + "?id_token=" + googleToken;

        ResponseEntity<GoogleTokenPayload> response = restTemplate.getForEntity(tokenInfoUrl, GoogleTokenPayload.class);
        Optional<ReferredCandidate> existingCandidate = referredCandidateRepository.findByPanNumber(referredCandidate.getPanNumber());

        if(response.getBody()==null){
            throw new IllegalStateException("User email failed");
        }

        if (existingCandidate.isPresent()) {
            throw new IllegalStateException("Duplicate PAN number found: " + referredCandidate.getPanNumber());
        }

        var candidate = ReferredCandidate.builder().
                dateOfReferral(LocalDateTime.now()).
                candidateName(referredCandidate.getCandidateName()).
                referrerEmail(response.getBody().getEmail()).
                primarySkill(referredCandidate.getPrimarySkill()).
                candidateEmail(referredCandidate.getCandidateEmail()).
                experience(referredCandidate.getExperience()).
                contactNumber(referredCandidate.getContactNumber()).
                currentStatus(referredCandidate.getCurrentStatus()).
                panNumber(referredCandidate.getPanNumber()).
                willingToRelocate(referredCandidate.isWillingToRelocate()).
                interviewStatus(referredCandidate.getInterviewStatus()).
                interviewedPosition(referredCandidate.getInterviewedPosition()).
                currentLocation(referredCandidate.getCurrentLocation()).
                businessUnit(referredCandidate.getBusinessUnit()).build();


        // If no duplicacy and email valid, save the new referred candidate
        referredCandidateRepository.save(candidate);
    }

    public ResponseEntity<Map<String,Object>> getReferredCandidatesOfUser(String googleToken){

        try{
            RestTemplate restTemplate = new RestTemplate();
            String tokenInfoUrl = googleTokenInfoUrl + "?id_token=" + googleToken;

            ResponseEntity<GoogleTokenPayload> response = restTemplate.getForEntity(tokenInfoUrl, GoogleTokenPayload.class);

            if(response.getBody()!=null)
            {
                Optional<List<ReferredCandidate>> referredCandidates = referredCandidateRepository.findByReferrerEmail(response.getBody().getEmail());
                Map<String,Object> referredCandidatesJson = new HashMap<>();

                if(referredCandidates.isPresent()){
                    List<ReferredCandidate> referredCandidatesList = referredCandidates.get();

                    referredCandidatesJson.put("referredCandidates",referredCandidatesList);
                    return ResponseEntity.ok(referredCandidatesJson);
                }else{
                    referredCandidatesJson.put("referredCandidates",null);
                    return ResponseEntity.ok(referredCandidatesJson);
                }
            }else{
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("status", "error");
                errorMap.put("message", "Invalid Google token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
            }
        }catch(Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getAllCandidates() {

        try{
            Map<String,Object> responseJson = new HashMap<>();
            List<ReferredCandidate> allReferredCandidates = referredCandidateRepository.findAll();

            responseJson.put("candidates",allReferredCandidates);

            return ResponseEntity.ok(responseJson);
        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }
}
