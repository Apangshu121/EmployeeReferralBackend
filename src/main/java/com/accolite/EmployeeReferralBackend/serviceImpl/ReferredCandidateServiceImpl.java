package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.models.CandidateDetails;
import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.models.SelectedReferredCandidate;
import com.accolite.EmployeeReferralBackend.repository.ReferredCandidateRepository;
import com.accolite.EmployeeReferralBackend.repository.SelectedReferredCandidateRepository;
import com.accolite.EmployeeReferralBackend.service.ReferredCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ReferredCandidateServiceImpl implements ReferredCandidateService {

    @Autowired
    ReferredCandidateRepository referredCandidateRepository;

    @Autowired
    SelectedReferredCandidateRepository selectedReferredCandidateRepository;

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
                    businessUnit(referredCandidate.getBusinessUnit()).
                    band(referredCandidate.getBand())
                    .build();


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

            List<ReferredCandidate> referredCandidates = referredCandidateRepository.findByReferrerEmail(email);
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

    @Override
    public ResponseEntity<Map<String, Object>> updateReferredCandidate(int id, ReferredCandidate updatedReferredCandidate) {
        try {

            ReferredCandidate referredCandidate = referredCandidateRepository.findById(id).orElseThrow();
            // Editable by Recruiter:- currentStatus, interviewStatus, interviewedPosition, businessUnit, band

            if (updatedReferredCandidate.getCurrentStatus() != null) {
                referredCandidate.setCurrentStatus(updatedReferredCandidate.getCurrentStatus().toUpperCase());
            }

            if (updatedReferredCandidate.getInterviewStatus() != null) {
                referredCandidate.setInterviewStatus(updatedReferredCandidate.getInterviewStatus().toUpperCase());
            }

            if (updatedReferredCandidate.getInterviewedPosition() != null){
                referredCandidate.setInterviewedPosition(updatedReferredCandidate.getInterviewedPosition());
            }

            if(updatedReferredCandidate.getBusinessUnit()!=null) {
                referredCandidate.setBusinessUnit(updatedReferredCandidate.getBusinessUnit());
            }

            if(updatedReferredCandidate.getBand()!=null) {
                referredCandidate.setBand(updatedReferredCandidate.getBand().toUpperCase());
            }

            ReferredCandidate savedReferredCandidate = referredCandidateRepository.save(referredCandidate);

            System.out.println(savedReferredCandidate);

            Optional<SelectedReferredCandidate> selectedReferredCandidateOpt = selectedReferredCandidateRepository.findByPanNumber(savedReferredCandidate.getPanNumber());

//       System.out.println(savedReferredCandidate.getCurrentStatus().equals("SELECT"));

            if(savedReferredCandidate.getCurrentStatus().equals("SELECT") && selectedReferredCandidateOpt.isEmpty())
            {

                if(referredCandidate.getBand() == null)
                {
                    Map<String, Object> errorMap = new HashMap<>();
                    errorMap.put("status", "error");
                    errorMap.put("message", "Band not set");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
                }

                String band = referredCandidate.getBand();

                double bonus = calculateBonus(band);

                var selectedReferredCandidate = SelectedReferredCandidate.builder().
                        name(referredCandidate.getCandidateName()).
                        panNumber(referredCandidate.getPanNumber()).
                        dateOfSelection(LocalDate.now()).
                        interviewedRole(referredCandidate.getInterviewedPosition()).
                        bonus(bonus).
                        bonusAllocated(false).
                        referrerEmail(referredCandidate.getReferrerEmail()).
                        currentlyInCompany(true).build();

                System.out.println(selectedReferredCandidate);

                selectedReferredCandidateRepository.save(selectedReferredCandidate);
            }

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
    @Override
    public ResponseEntity<Map<String,Object>> filterCandidatesByExperience(int experience) {
        try {
            List<ReferredCandidate> filteredCandidates = referredCandidateRepository.findByExperienceGreaterThanEqual(experience);
            Map<String, Object> responseJson = new HashMap<>();

            responseJson.put("Filtered Candidates", filteredCandidates);
            return ResponseEntity.ok(responseJson);
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }
    @Override
    public ResponseEntity<Map<String, Object>> filterCandidatesByPreferredLocation(String preferredLocation) {
        try {
            List<ReferredCandidate> filteredCandidates = referredCandidateRepository.findByPreferredLocation(preferredLocation);
            Map<String, Object> responseJson = new HashMap<>();

            responseJson.put("Filtered Candidates", filteredCandidates);
            return ResponseEntity.ok(responseJson);
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    @Override
    public ResponseEntity<Map<String,Object>> filterCandidatesByNoticePeriodLessThanOrEqual(int noticePeriod) {
        try {
            List<ReferredCandidate> filteredCandidates = referredCandidateRepository.findByNoticePeriodLessThanOrEqual(noticePeriod);
            Map<String, Object> responseJson = new HashMap<>();

            responseJson.put("Filtered Candidates", filteredCandidates);
            return ResponseEntity.ok(responseJson);
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }
    private double calculateBonus(String band) {

        switch (band) {
            case "B7":
                return 50000;
            case "B6":
                return 75000;
            case "B5L":
            case "B5H":
            case "B4L":
                return 100000;
            case "B4H":
            case "B3":
            case "B2":
            case "B1":
                return 150000;
            default:
                return 0; // Default case if the band is not recognized
        }
    }
}
