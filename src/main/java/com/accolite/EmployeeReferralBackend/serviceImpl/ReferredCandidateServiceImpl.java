package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.models.CandidateDetails;
import com.accolite.EmployeeReferralBackend.models.GoogleTokenPayload;
import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.repository.ReferredCandidateRepository;
import com.accolite.EmployeeReferralBackend.service.ReferredCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ReferredCandidateServiceImpl implements ReferredCandidateService {

    private final String googleTokenInfoUrl = "https://www.googleapis.com/oauth2/v3/tokeninfo";

    @Autowired
    ReferredCandidateRepository referredCandidateRepository;

    public ResponseEntity<Map<String, Object>> addReferredCandidate(ReferredCandidate referredCandidate) {

        try{
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = ((UserDetails)principal).getUsername();


            if(!isValidPanCard(referredCandidate.getPanNumber(),referredCandidate.getCandidateName()))
            {
                throw new IllegalStateException("Not a valid Pan Card");
            }

            Optional<ReferredCandidate> existingCandidate = referredCandidateRepository.findByPanNumber(referredCandidate.getPanNumber());

            if (existingCandidate.isPresent()) {
                throw new IllegalStateException("Duplicate PAN number found: " + referredCandidate.getPanNumber());
            }

            var candidate = ReferredCandidate.builder().
                    dateOfReferral(LocalDateTime.now()).
                    candidateName(referredCandidate.getCandidateName()).
                    referrerEmail(email).
                    primarySkill(referredCandidate.getPrimarySkill()).
                    secondarySkills(referredCandidate.getSecondarySkills()).
                    candidateEmail(referredCandidate.getCandidateEmail()).
                    experience(referredCandidate.getExperience()).
                    contactNumber(referredCandidate.getContactNumber()).
                    currentStatus(referredCandidate.getCurrentStatus()).
                    panNumber(referredCandidate.getPanNumber()).
                    willingToRelocate(referredCandidate.isWillingToRelocate()).
                    interviewStatus(referredCandidate.getInterviewStatus()).
                    interviewedPosition(referredCandidate.getInterviewedPosition()).
                    preferredLocation(referredCandidate.getPreferredLocation()).
                    noticePeriod(referredCandidate.getNoticePeriod()).
                    businessUnit(referredCandidate.getBusinessUnit()).build();


            // If no duplicacy and email valid, save the new referred candidate
            referredCandidateRepository.save(candidate);

            Map<String,Object> responseMap = new HashMap<>();
            responseMap.put("message","Referred Candidate added Successfully");

            return ResponseEntity.ok(responseMap);
        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    public static boolean isValidPanCard(String panNumber, String personName) {

        String[] nameParts = personName.split("\\s+");
        String surname = nameParts[nameParts.length - 1].toUpperCase();

        System.out.println(surname);
        String panPattern = String.format("[A-Z]{3}P%c[0-9]{4}[A-Z]{1}", surname.charAt(0));

        Pattern pattern = Pattern.compile(panPattern);

        Matcher matcher = pattern.matcher(panNumber);

        return matcher.matches();
    }

    public ResponseEntity<Map<String,Object>> getReferredCandidatesOfUser(){

        try{
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = ((UserDetails)principal).getUsername();

            List<CandidateDetails> referredCandidates = referredCandidateRepository.findAllCandidatesOfReferrer(email);
            Map<String,Object> referredCandidatesJson = new HashMap<>();

            referredCandidatesJson.put("referredCandidates",referredCandidates);
            return ResponseEntity.ok(referredCandidatesJson);

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
                List<CandidateDetails> allReferredCandidates = referredCandidateRepository.findAllCandidates();

            responseJson.put("candidates",allReferredCandidates);

            return ResponseEntity.ok(responseJson);
        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getCandidateById(int id) {

        try{
            Map<String, Object> responseJson = new HashMap<>();
            Optional<ReferredCandidate> referredCandidate = referredCandidateRepository.findById(id);

            referredCandidate.ifPresent(candidate -> responseJson.put("candidate", candidate));

            return ResponseEntity.ok(responseJson);
        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }
}
