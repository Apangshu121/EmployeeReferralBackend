package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.models.ReferredCandidateHistory;
import com.accolite.EmployeeReferralBackend.models.SelectedReferredCandidate;
import com.accolite.EmployeeReferralBackend.repository.ReferredCandidateHistoryRepository;
import com.accolite.EmployeeReferralBackend.repository.ReferredCandidateRepository;
import com.accolite.EmployeeReferralBackend.repository.SelectedReferredCandidateRepository;
import com.accolite.EmployeeReferralBackend.service.GoogleSheetsService;
import com.accolite.EmployeeReferralBackend.service.ReferredCandidateService;
import com.accolite.EmployeeReferralBackend.utils.GoogleSheetsProperties;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private ReferredCandidateHistoryRepository referredCandidateHistoryRepository;

    @Autowired
    SelectedReferredCandidateRepository selectedReferredCandidateRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

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
                    currentStatus(referredCandidate.getCurrentStatus().toUpperCase()).
                    panNumber(referredCandidate.getPanNumber()).
                    willingToRelocate(referredCandidate.isWillingToRelocate()).
                    interviewStatus(referredCandidate.getInterviewStatus().toUpperCase()).
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
            if (updatedReferredCandidate.getInterviewStatus() != null
                    && !updatedReferredCandidate.getInterviewStatus().equalsIgnoreCase(referredCandidate.getInterviewStatus())) {
                ReferredCandidateHistory historyEntry = new ReferredCandidateHistory();
                historyEntry.setReferredCandidate(referredCandidate);
                historyEntry.setInterviewStatus(updatedReferredCandidate.getInterviewStatus().toUpperCase());
                historyEntry.setUpdateDate(LocalDate.now());
                referredCandidateHistoryRepository.save(historyEntry);
            }

            if (updatedReferredCandidate.getCurrentStatus() != null) {
                referredCandidate.setCurrentStatus(updatedReferredCandidate.getCurrentStatus().toUpperCase());
                referredCandidate.setCurrentStatusUpdated(true);
            }

            if (updatedReferredCandidate.getInterviewStatus() != null) {
                referredCandidate.setInterviewStatus(updatedReferredCandidate.getInterviewStatus().toUpperCase());
                referredCandidate.setInterviewStatusUpdated(true);
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


            try{
                if(savedReferredCandidate.getCurrentStatus().equals("SELECT"))
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
                            dateOfSelection(LocalDate.now()).
                            interviewedRole(referredCandidate.getInterviewedPosition()).
                            bonus(bonus).
                            bonusAllocated(false).
                            referrerEmail(referredCandidate.getReferrerEmail()).
                            referredCandidate(savedReferredCandidate).
                            currentlyInCompany(true).build();

                    System.out.println(selectedReferredCandidate);

                    selectedReferredCandidateRepository.save(selectedReferredCandidate);
                }
            }catch (Exception e){
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("status", "error");
                errorMap.put("message", "Candidate current status is already set to SELECT");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
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

    @Override
    public ResponseEntity<Map<String, Object>> sendMail(int id) {

        try {
           ReferredCandidate referredCandidate = referredCandidateRepository.findById(id).orElseThrow();

           boolean flag1=false;
           boolean flag2=false;

           if(referredCandidate.isInterviewStatusUpdated()){
               referredCandidate.setInterviewStatusUpdated(false);
               flag1=true;
               SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
               simpleMailMessage.setFrom(fromMail);
               String subject= getSubjectOfCandidate(referredCandidate.getInterviewStatus());
               String template = getTemplateOfCandidate(referredCandidate.getInterviewStatus(),referredCandidate.getCandidateName());
               simpleMailMessage.setSubject(subject);
               simpleMailMessage.setText(template);
               simpleMailMessage.setTo(referredCandidate.getCandidateEmail());
               mailSender.send(simpleMailMessage);

               SimpleMailMessage simpleMailMessage1 = new SimpleMailMessage();
               simpleMailMessage1.setFrom(fromMail);
               String subject1= getSubjectOfReferrer(referredCandidate.getInterviewStatus(),referredCandidate.getCandidateName());
               String template1 = getTemplateOfReferrer(referredCandidate.getInterviewStatus(),referredCandidate.getCandidateName());
               simpleMailMessage1.setSubject(subject1);
               simpleMailMessage1.setText(template1);
               simpleMailMessage1.setTo(referredCandidate.getReferrerEmail());
               mailSender.send(simpleMailMessage1);
           }

           if(referredCandidate.isCurrentStatusUpdated()){
               referredCandidate.setCurrentStatusUpdated(false);
               flag2=true;
               SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
               simpleMailMessage.setFrom(fromMail);
               String subject= getSubjectOfCandidateForCurrentStatus(referredCandidate.getCandidateName());
               String template = getTemplateOfCandidateForCurrentStatus(referredCandidate.getCurrentStatus(),referredCandidate.getCandidateName());
               simpleMailMessage.setSubject(subject);
               simpleMailMessage.setText(template);
               simpleMailMessage.setTo(referredCandidate.getCandidateEmail());
               mailSender.send(simpleMailMessage);

               SimpleMailMessage simpleMailMessage1 = new SimpleMailMessage();
               simpleMailMessage1.setFrom(fromMail);
               String subject1= getSubjectOfReferrerForCandidateStatus(referredCandidate.getCandidateName());
               String template1 = getTemplateOfReferrerForCandidateForCandidateStatus(referredCandidate.getCurrentStatus(),referredCandidate.getCandidateName());
               simpleMailMessage1.setSubject(subject1);
               simpleMailMessage1.setText(template1);
               simpleMailMessage1.setTo(referredCandidate.getReferrerEmail());
               mailSender.send(simpleMailMessage1);
           }

           if(!flag1 && !flag2){
               Map<String, Object> errorMap = new HashMap<>();
               errorMap.put("status", "error");
               errorMap.put("message", "Mail already sent to both the referrer and candidate about the status of their recruitment process");
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
           }

           referredCandidateRepository.save(referredCandidate);

            Map<String, Object> responseJson = new HashMap<>();

            responseJson.put("Filtered Candidates", "Mails are sent successfully to both candidate and referrer");

            return ResponseEntity.ok(responseJson);

        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> searchCandidates(String keyword) {
        try{
            Specification<ReferredCandidate> specification = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (keyword != null && !keyword.isEmpty()) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("candidateName")), "%" + keyword.toLowerCase() + "%"));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

            List<ReferredCandidate> searchedCandidatesList = referredCandidateRepository.findAll(specification);

            Map<String, Object> responseJson = new HashMap<>();

            responseJson.put("Searched Candidates", searchedCandidatesList);

            return ResponseEntity.ok(responseJson);
        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }



    private String getTemplateOfReferrerForCandidateForCandidateStatus(String currentStatus, String candidateName) {
        String greeting = "Hello " + "Referrer" + ",\n\n";
        String content = switch (currentStatus.toUpperCase()) {
            case "SELECT" ->
                    "Great news! The candidate you referred, " + candidateName + ", has been selected for the position.";
            case "REJECT" -> "We regret to inform you that the candidate you referred, " +
                    candidateName + ", did not pass the interview process and won't be moving forward.";
            case "DROP" -> "The application of the candidate you referred, " + candidateName +
                    ", has been dropped for the current position.";
            case "ON HOLD" -> "The application of the candidate you referred, " + candidateName +
                    ", is currently on hold. We will provide further updates soon.";
            case "BETTER QUALIFIED FOR OTHER POSITION" -> "While the candidate you referred, " + candidateName +
                    ", was not selected for the current position, we believe they are better qualified for another position.";
            default -> "The status of the candidate you referred, " + candidateName +
                    ", has been updated to " + currentStatus + ". Please check the candidate's application status.";
        };

        return greeting + content + "\n\nBest regards,\nThe Interview Team";
    }

    private String getSubjectOfReferrerForCandidateStatus(String candidateName) {
        String subject = "Application Status Update for Referred Candidate: " + candidateName;

        return subject;
    }

    private String getTemplateOfCandidateForCurrentStatus(String currentStatus, String candidateName) {
        String greeting = "Hello " + candidateName + ",\n\n";
        String content = switch (currentStatus.toUpperCase()) {
            case "SELECT" -> "Congratulations! You have been selected for the position.";
            case "REJECT" -> "We appreciate your effort, but unfortunately, you did not pass the interview process.";
            case "DROP" -> "Your application has been dropped for the current position.";
            case "ON HOLD" -> "Your application is currently on hold. We will provide further updates soon.";
            case "BETTER QUALIFIED FOR OTHER POSITION" ->
                    "While you were not selected for the current position, we believe you are better qualified for another position.";
            default -> "Your status has been updated to " + currentStatus + ". Please check your application status.";
        };

        return greeting + content + "\n\nBest regards,\nThe Interview Team";
    }

    private String getSubjectOfCandidateForCurrentStatus(String candidateName) {

        return "Application Status Update for " + candidateName;
    }

    private String getTemplateOfReferrer(String interviewStatus, String candidateName) {
        String greeting = "Hello Referrer,\n\n";
        String content = switch (interviewStatus.toUpperCase()) {
            case "CODELYSER SELECT" -> "We are pleased to inform you that the candidate you referred, " +
                    candidateName + ", has passed the Codelyser assessment test and has been selected for the next round.";
            case "R1 SELECT" -> "Great news! The candidate you referred, " + candidateName +
                    ", has successfully passed Round 1 of the interview and has been selected for the next round.";
            case "R2 SELECT" -> "Exciting news! The candidate you referred, " + candidateName +
                    ", has passed Round 2 of the interview and has been selected for the next round.";
            case "R3 SELECT" -> "Congratulations! The candidate you referred, " + candidateName +
                    ", has passed Round 3 of the interview.";
            case "CODELYSER REJECT", "R1 REJECT", "R2 REJECT", "R3 REJECT" ->
                    "We regret to inform you that the candidate you referred, " +
                            candidateName + ", did not pass the assessment/interview and won't be moving forward.";
            default -> "The status of the candidate you referred, " +
                    candidateName + ", has been updated. Please check the candidate's status.";
        };

        return greeting + content + "\n\nBest regards,\nThe Interview Team";
    }

    private String getSubjectOfReferrer(String interviewStatus, String candidateName) {
        String subject = "Candidate Status Update for " + candidateName;

        return subject;
    }

    private String getTemplateOfCandidate(String interviewStatus, String candidateName) {
        String greeting = "Hello "+candidateName+",\n\n";
        String content = switch (interviewStatus.toUpperCase()) {
            case "CODELYSER SELECT" ->
                    "Congratulations on passing the Codelyser assessment test! You have been selected for the next round.";
            case "R1 SELECT" ->
                    "Congratulations on passing Round 1 of the interview! You have been selected for the next round.";
            case "R2 SELECT" ->
                    "Congratulations on passing Round 2 of the interview! You have been selected for the next round.";
            case "R3 SELECT" ->
                    "Congratulations on passing Round 3 of the interview! You have been selected for the next round.";
            case "CODELYSER REJECT" ->
                    "We appreciate your effort, but unfortunately, you did not pass the Codelyser assessment test.";
            case "R1 REJECT" ->
                    "We appreciate your effort, but unfortunately, you did not pass Round 1 of the interview.";
            case "R2 REJECT" ->
                    "We appreciate your effort, but unfortunately, you did not pass Round 2 of the interview.";
            case "R3 REJECT" ->
                    "We appreciate your effort, but unfortunately, you did not pass Round 3 of the interview.";
            default -> "Your interview status has been updated.";
        };

        return greeting + content + "\n\nBest regards,\nThe Interview Team";
    }

    private String getSubjectOfCandidate(String interviewStatus) {
        return switch (interviewStatus.toUpperCase()) {
            case "CODELYSER SELECT", "R1 SELECT", "R2 SELECT", "R3 SELECT" ->
                    "Congratulations! You have been selected for the next round";
            case "CODELYSER REJECT", "R1 REJECT", "R2 REJECT", "R3 REJECT" ->
                    "We appreciate your effort, but we won't be moving forward";
            default -> "Interview Status Update";
        };
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
