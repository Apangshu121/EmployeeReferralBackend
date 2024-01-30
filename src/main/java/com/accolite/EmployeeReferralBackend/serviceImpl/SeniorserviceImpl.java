package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.dtos.ReferredCandidateDTO;
import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.models.User;
import com.accolite.EmployeeReferralBackend.repository.ReferredCandidateRepository;
import com.accolite.EmployeeReferralBackend.repository.UserRepository;
import com.accolite.EmployeeReferralBackend.service.SeniorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SeniorserviceImpl implements SeniorService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReferredCandidateRepository referredCandidateRepository;

    @Override
    public ResponseEntity<Map<String, Object>> getReferredCandidatesOfBusinessUnit() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = ((UserDetails)principal).getUsername();

            User user = userRepository.findByEmail(email).orElseThrow();

            List<ReferredCandidate> referredCandidates = referredCandidateRepository.findByBusinessUnitAndInterviewStatusCurrentStatusAndInterviewStatusIsNotNull(user.getBusinessUnit(), "SELECT");
            List<ReferredCandidateDTO> allReferredCandidatesDTOS = referredCandidates.stream()
                    .map(this::mapToReferredCandidateDTO)
                    .toList();
            Map<String,Object> responseJson = new HashMap<>();
            responseJson.put("candidates",allReferredCandidatesDTOS);

            return ResponseEntity.ok(responseJson);
        } catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }

    }

    private ReferredCandidateDTO mapToReferredCandidateDTO(ReferredCandidate candidate) {

        return ReferredCandidateDTO.builder()
                .id(candidate.getId())
                .primarySkill(candidate.getPrimarySkill())
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
                .referrerEmail(candidate.getReferrerEmail())
                .referredCandidateHistories(candidate.getReferredCandidateHistory())
                .build();
    }
}
