package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.models.*;
import com.accolite.EmployeeReferralBackend.repository.ReferredCandidateHistoryRepository;
import com.accolite.EmployeeReferralBackend.repository.ReferredCandidateRepository;
import com.accolite.EmployeeReferralBackend.repository.SelectedReferredCandidateRepository;
import com.accolite.EmployeeReferralBackend.repository.UserRepository;
import com.accolite.EmployeeReferralBackend.service.ReferredCandidateService;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Transactional
public class ReferredCandidateServiceImpl implements ReferredCandidateService {

    @Autowired
    UserRepository userRepository;

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
            User user = userRepository.findByEmail(email).orElseThrow();

            if(!isValidPanCard(referredCandidate.getPanNumber(),referredCandidate.getCandidateName()))
            {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("status", "error");
                errorMap.put("message", "Not a valid Pan Card");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
            }

            Optional<ReferredCandidate> existingCandidateOpt = referredCandidateRepository.findByPanNumber(referredCandidate.getPanNumber());
            LocalDateTime currentDateTime = LocalDateTime.now();


            if (existingCandidateOpt.isPresent() && currentDateTime.isBefore(existingCandidateOpt.get().getUpdatedAt().plus(6, ChronoUnit.MONTHS))) {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("status", "error");
                errorMap.put("message", "The candidate has been referred within the last 6 months");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
            }else if(existingCandidateOpt.isPresent() && currentDateTime.isAfter(existingCandidateOpt.get().getUpdatedAt().plus(6, ChronoUnit.MONTHS)) && existingCandidateOpt.get().getNoOfTimesReferred()==3){
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("status", "error");
                errorMap.put("message", "The candidate has already been referred 3 times");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
            }else{
                if(existingCandidateOpt.isEmpty()){
                    ReferredCandidate candidate = new ReferredCandidate();

                    candidate.setPrimarySkill(referredCandidate.getPrimarySkill());
                    candidate.setSecondarySkills(referredCandidate.getSecondarySkills());
                    candidate.setCandidateName(referredCandidate.getCandidateName());
                    candidate.setExperience(referredCandidate.getExperience());
                    candidate.setContactNumber(referredCandidate.getContactNumber());
                    candidate.setCandidateEmail(referredCandidate.getCandidateEmail());
                    candidate.setPanNumber(referredCandidate.getPanNumber());
                    candidate.setWillingToRelocate(referredCandidate.isWillingToRelocate());
                    candidate.setPreferredLocation(referredCandidate.getPreferredLocation());
                    candidate.setNoticePeriod(referredCandidate.getNoticePeriod());
                    candidate.setProfileSource(referredCandidate.getProfileSource());
                    candidate.setVouch(referredCandidate.isVouch());
                    candidate.setNoticePeriodLeft(referredCandidate.getNoticePeriodLeft());
                    candidate.setServingNoticePeriod(referredCandidate.isServingNoticePeriod());
                    candidate.setOfferInHand(referredCandidate.isOfferInHand());
                    candidate.setNoOfTimesReferred(1);
                    candidate.setUpdatedAt(LocalDateTime.now());
                    candidate.setActive(true);


                    user.getReferredCandidates().add(candidate);
                    candidate.setUser(user);

                    referredCandidateRepository.save(candidate);
                    // System.out.println(user);
                    userRepository.save(user);
                    Map<String,Object> responseMap = new HashMap<>();
                    responseMap.put("message","Referred Candidate added Successfully");

                    return ResponseEntity.ok(responseMap);
                }else{
                    ReferredCandidate existingCandidate = existingCandidateOpt.get();
                    existingCandidate.setPrimarySkill(referredCandidate.getPrimarySkill());
                    existingCandidate.setSecondarySkills(referredCandidate.getSecondarySkills());
                    existingCandidate.setCandidateName(referredCandidate.getCandidateName());
                    existingCandidate.setExperience(referredCandidate.getExperience());
                    existingCandidate.setContactNumber(referredCandidate.getContactNumber());
                    existingCandidate.setCandidateEmail(referredCandidate.getCandidateEmail());
                    existingCandidate.setPanNumber(referredCandidate.getPanNumber());
                    existingCandidate.setWillingToRelocate(referredCandidate.isWillingToRelocate());
                    existingCandidate.setPreferredLocation(referredCandidate.getPreferredLocation());
                    existingCandidate.setNoticePeriod(referredCandidate.getNoticePeriod());
                    existingCandidate.setProfileSource(referredCandidate.getProfileSource());
                    existingCandidate.setVouch(referredCandidate.isVouch());
                    existingCandidate.setNoticePeriodLeft(referredCandidate.getNoticePeriodLeft());
                    existingCandidate.setServingNoticePeriod(referredCandidate.isServingNoticePeriod());
                    existingCandidate.setOfferInHand(referredCandidate.isOfferInHand());
                    existingCandidate.setNoOfTimesReferred(existingCandidate.getNoOfTimesReferred()+1);
                    existingCandidate.setUpdatedAt(LocalDateTime.now());

                    user.getReferredCandidates().add(existingCandidate);
                    existingCandidate.setUser(user);
                    // referredCandidateRepository.save(existingCandidate);

                    userRepository.save(user);
                    Map<String,Object> responseMap = new HashMap<>();
                    responseMap.put("message","Referred Candidate updated Successfully");

                    return ResponseEntity.ok(responseMap);
                }
            }

        }catch (Exception e){
            // e.printStackTrace();
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

            User user = userRepository.findByEmail(email).orElseThrow();
            //System.out.println(user);
            Set<ReferredCandidate> referredCandidates = user.getReferredCandidates();
            List<ReferredCandidateDTO> referredCandidateDTOS = referredCandidates.stream()
                    .map(referredCandidate -> mapToReferredCandidateDTO(referredCandidate,user))
                    .toList();
            Map<String,Object> referredCandidatesJson = new HashMap<>();

            referredCandidatesJson.put("referredCandidates",referredCandidateDTOS);
            return ResponseEntity.ok(referredCandidatesJson);

        }catch(Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    private ReferredCandidateDTO mapToReferredCandidateDTO(ReferredCandidate candidate, User user) {

        return ReferredCandidateDTO.builder()
                .id(candidate.getId())
                .primarySkill(candidate.getPrimarySkill())
                .secondarySkills(candidate.getSecondarySkills())
                .candidateName(candidate.getCandidateName())
                .experience(candidate.getExperience())
                .contactNumber(candidate.getContactNumber())
                .candidateEmail(candidate.getCandidateEmail())
                .willingToRelocate(candidate.isWillingToRelocate())
                .interviewedPosition(candidate.getInterviewedPosition())
                .interviewTheCandidate(candidate.isInterviewTheCandidate())
                .preferredLocation(candidate.getPreferredLocation())
                .businessUnit(candidate.getBusinessUnit())
                .band(candidate.getBand())
                .vouch(candidate.isVouch())
                .servingNoticePeriod(candidate.isServingNoticePeriod())
                .noticePeriodLeft(candidate.getNoticePeriodLeft())
                .offerInHand(candidate.isOfferInHand())
                .interviewStatus(candidate.getInterviewStatus())
                .referrerEmail(user.getEmail())
                .referredCandidateHistories(candidate.getReferredCandidateHistory())
                .build();
    }

    @Override
    public ResponseEntity<Map<String, Object>> getAllCandidates() {

        try{
            Map<String,Object> responseJson = new HashMap<>();
            List<ReferredCandidate> allReferredCandidates = referredCandidateRepository.findAll();
            List<ReferredCandidateDTO> referredCandidateDTOS = allReferredCandidates.stream()
                    .map(candidate -> mapToReferredCandidateDTO(candidate, candidate.getUser()))
                    .toList();

            responseJson.put("candidates",referredCandidateDTOS);

            return ResponseEntity.ok(responseJson);
        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> interviewTheCandidate(int id) {
        try{
            ReferredCandidate referredCandidate = referredCandidateRepository.findById(id).orElseThrow();
            referredCandidate.setInterviewTheCandidate(true);
            InterviewStatus interviewStatus = new InterviewStatus();
            interviewStatus.setCurrentStatus("POOL");
            interviewStatus.setInterviewStatus("POOL");
            referredCandidate.setInterviewStatus(interviewStatus);
            referredCandidate.setUpdatedAt(LocalDateTime.now());
            referredCandidateRepository.save(referredCandidate);
            Map<String,Object> responseMap = new HashMap<>();
            responseMap.put("message","Candidate selected for interview");

            return ResponseEntity.ok(responseMap);
        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

//    @Override
//    public ResponseEntity<Map<String, Object>> getCandidateById(int id) {
//
//        try{
//            Map<String, Object> responseJson = new HashMap<>();
//            Optional<ReferredCandidate> referredCandidate = referredCandidateRepository.findById(id);
//
//            referredCandidate.ifPresent(candidate -> responseJson.put("candidate", candidate));
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
    public ResponseEntity<Map<String, Object>> updateReferredCandidate(int id, UpdateReferredCandidateRequestDTO updatedReferredCandidate) {
        try {

            ReferredCandidate referredCandidate = referredCandidateRepository.findById(id).orElseThrow();
            // Editable by Recruiter:- currentStatus, interviewStatus, interviewedPosition, businessUnit, band
            if (updatedReferredCandidate.getInterviewStatus() != null
                    && !updatedReferredCandidate.getInterviewStatus().equalsIgnoreCase(referredCandidate.getInterviewStatus().getInterviewStatus())) {
                ReferredCandidateHistory historyEntry = new ReferredCandidateHistory();
                historyEntry.setInterviewStatus(updatedReferredCandidate.getInterviewStatus().toUpperCase());
                historyEntry.setUpdateDate(LocalDateTime.now());
                historyEntry.setReferredCandidate(referredCandidate);
                referredCandidate.getReferredCandidateHistory().add(historyEntry);
            }

            InterviewStatus interviewStatus = referredCandidate.getInterviewStatus();

            if (updatedReferredCandidate.getCurrentStatus() != null) {
                interviewStatus.setCurrentStatus(updatedReferredCandidate.getCurrentStatus().toUpperCase());
                interviewStatus.setCurrentStatusUpdated(true);
            }

            if (updatedReferredCandidate.getInterviewStatus() != null) {
                interviewStatus.setInterviewStatus(updatedReferredCandidate.getInterviewStatus().toUpperCase());
                interviewStatus.setInterviewStatusUpdated(true);
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

            referredCandidate.setUpdatedAt(LocalDateTime.now());
            referredCandidateRepository.save(referredCandidate);

            ReferredCandidate savedReferredCandidate = referredCandidateRepository.findById(id).orElseThrow();
            Optional<SelectedReferredCandidate> selectedReferredCandidateOpt = selectedReferredCandidateRepository.findByEmail(referredCandidate.getCandidateEmail());

            try{
                if(selectedReferredCandidateOpt.isEmpty() && savedReferredCandidate.getInterviewStatus().getCurrentStatus().equals("SELECT"))
                {

                    if(savedReferredCandidate.getBand() == null)
                    {
                        Map<String, Object> errorMap = new HashMap<>();
                        errorMap.put("status", "error");
                        errorMap.put("message", "Band not set");
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
                    }

                    if(savedReferredCandidate.getInterviewedPosition() == null)
                    {
                        Map<String, Object> errorMap = new HashMap<>();
                        errorMap.put("status", "error");
                        errorMap.put("message", "Interviewed Position of candidate not set");
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
                    }

                    String band = savedReferredCandidate.getBand();

                    double bonus = calculateBonus(band);

                    var selectedReferredCandidate = SelectedReferredCandidate.builder().
                            name(savedReferredCandidate.getCandidateName()).
                            phoneNumber(savedReferredCandidate.getContactNumber()).
                            email(savedReferredCandidate.getCandidateEmail()).
                            interviewedRole(savedReferredCandidate.getInterviewedPosition()).
                            bonus(bonus).
                            bonusAllocated(false).
                            referrerEmail(savedReferredCandidate.getUser().getEmail()).
                            currentlyInCompany(true).build();

                    savedReferredCandidate.setSelectedReferredCandidate(selectedReferredCandidate);
                    savedReferredCandidate.setUpdatedAt(LocalDateTime.now());
                    referredCandidateRepository.save(savedReferredCandidate);
                    System.out.println(selectedReferredCandidate);
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
            e.printStackTrace();
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
            List<ReferredCandidateDTO> referredCandidateDTOS = filteredCandidates.stream()
                    .map(candidate -> mapToReferredCandidateDTO(candidate, candidate.getUser()))
                    .toList();

            Map<String, Object> responseJson = new HashMap<>();

            responseJson.put("Filtered Candidates", referredCandidateDTOS);
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
            List<ReferredCandidateDTO> referredCandidateDTOS = filteredCandidates.stream()
                    .map(candidate -> mapToReferredCandidateDTO(candidate, candidate.getUser()))
                    .toList();
            Map<String, Object> responseJson = new HashMap<>();

            responseJson.put("Filtered Candidates", referredCandidateDTOS);
            return ResponseEntity.ok(responseJson);
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    @Override
    public ResponseEntity<Map<String,Object>> filterCandidatesByNoticePeriodLessThanOrEqual(int noticePeriodLeft) {
        try {
            List<ReferredCandidate> filteredCandidates = referredCandidateRepository.findByNoticePeriodLeftLessThanOrEqual(noticePeriodLeft);
            List<ReferredCandidateDTO> referredCandidateDTOS = filteredCandidates.stream()
                    .map(candidate -> mapToReferredCandidateDTO(candidate, candidate.getUser()))
                    .toList();

            Map<String, Object> responseJson = new HashMap<>();

            responseJson.put("Filtered Candidates", referredCandidateDTOS);
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

            InterviewStatus interviewStatus = referredCandidate.getInterviewStatus();

            boolean flag1=false;
            boolean flag2=false;
            System.out.println(referredCandidate.getCandidateName()+" "+referredCandidate.getCandidateEmail()+" "+referredCandidate.getUser().getEmail());

            if(interviewStatus.isInterviewStatusUpdated()){
                interviewStatus.setInterviewStatusUpdated(false);
                flag1=true;
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setFrom(fromMail);
                String subject= getSubjectOfCandidate(interviewStatus.getInterviewStatus());
                String template = getTemplateOfCandidate(interviewStatus.getInterviewStatus(),referredCandidate.getCandidateName());
                simpleMailMessage.setSubject(subject);
                simpleMailMessage.setText(template);
                simpleMailMessage.setTo(referredCandidate.getCandidateEmail());
                mailSender.send(simpleMailMessage);

                SimpleMailMessage simpleMailMessage1 = new SimpleMailMessage();
                simpleMailMessage1.setFrom(fromMail);
                String subject1= getSubjectOfReferrer(interviewStatus.getInterviewStatus(),referredCandidate.getCandidateName());
                String template1 = getTemplateOfReferrer(interviewStatus.getInterviewStatus(),referredCandidate.getCandidateName());
                simpleMailMessage1.setSubject(subject1);
                simpleMailMessage1.setText(template1);
                simpleMailMessage1.setTo(referredCandidate.getUser().getEmail());
                mailSender.send(simpleMailMessage1);
            }

            if(interviewStatus.isCurrentStatusUpdated()){
                interviewStatus.setCurrentStatusUpdated(false);
                flag2=true;
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setFrom(fromMail);
                String subject= getSubjectOfCandidateForCurrentStatus(referredCandidate.getCandidateName());
                String template = getTemplateOfCandidateForCurrentStatus(interviewStatus.getCurrentStatus(),referredCandidate.getCandidateName());
                simpleMailMessage.setSubject(subject);
                simpleMailMessage.setText(template);
                simpleMailMessage.setTo(referredCandidate.getCandidateEmail());
                mailSender.send(simpleMailMessage);

                SimpleMailMessage simpleMailMessage1 = new SimpleMailMessage();
                simpleMailMessage1.setFrom(fromMail);
                String subject1= getSubjectOfReferrerForCandidateStatus(referredCandidate.getCandidateName());
                String template1 = getTemplateOfReferrerForCandidateForCandidateStatus(interviewStatus.getCurrentStatus(),referredCandidate.getCandidateName());
                simpleMailMessage1.setSubject(subject1);
                simpleMailMessage1.setText(template1);
                simpleMailMessage1.setTo(referredCandidate.getUser().getEmail());
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
            List<ReferredCandidateDTO> referredCandidateDTOS = searchedCandidatesList.stream()
                    .map(candidate -> mapToReferredCandidateDTO(candidate, candidate.getUser()))
                    .toList();

            Map<String, Object> responseJson = new HashMap<>();

            responseJson.put("Searched Candidates", referredCandidateDTOS);

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
