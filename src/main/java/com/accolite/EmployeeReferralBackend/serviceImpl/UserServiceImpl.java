package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.models.GoogleTokenPayload;
import com.accolite.EmployeeReferralBackend.models.ReferralTallyDTO;
import com.accolite.EmployeeReferralBackend.models.ReferredCandidate;
import com.accolite.EmployeeReferralBackend.models.User;
import com.accolite.EmployeeReferralBackend.repository.ReferredCandidateRepository;
import com.accolite.EmployeeReferralBackend.repository.UserRepository;
import com.accolite.EmployeeReferralBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReferredCandidateRepository referredCandidateRepository;

    @Value("${googleUrl}")
    private String googleTokenInfoUrl;

    @Override
    public ResponseEntity<Map<String, Object>> getDetailsOfUser(String token) {
        try{

            Map<String,Object> responseMap = new HashMap<>();

            if(token!=null)
            {
                RestTemplate restTemplate = new RestTemplate();
                // System.out.println(referredCandidateRequestDTO.getToken());
                String tokenInfoUrl = googleTokenInfoUrl + "?id_token=" + token;
                System.out.println(tokenInfoUrl);
                ResponseEntity<GoogleTokenPayload> response = restTemplate.getForEntity(tokenInfoUrl, GoogleTokenPayload.class);
                if(response.getBody()!=null) {
                    String name = response.getBody().getName();
                    responseMap.put("name",name);
                    responseMap.put("role", "EMPLOYEE");
                }else{
                    Map<String, Object> errorMap = new HashMap<>();
                    errorMap.put("status", "error");
                    errorMap.put("message", "Invalid Google token");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
                }
            }else{
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String email = ((UserDetails)principal).getUsername();
                User user = userRepository.findByEmail(email).orElseThrow();
                String name = user.getName();
                responseMap.put("name",name);
                responseMap.put("role", user.getRole());
            }

            return ResponseEntity.ok(responseMap);
        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getReferralTallyOfUser(String token) {
        try{

            String email="";
            String name="";

            if(token!=null)
            {
                RestTemplate restTemplate = new RestTemplate();
                String tokenInfoUrl = googleTokenInfoUrl + "?id_token=" + token;
                System.out.println(tokenInfoUrl);
                ResponseEntity<GoogleTokenPayload> response = restTemplate.getForEntity(tokenInfoUrl, GoogleTokenPayload.class);
                if(response.getBody()!=null) {
                    email = response.getBody().getEmail();
                    name = response.getBody().getName();
                }else{
                    Map<String, Object> errorMap = new HashMap<>();
                    errorMap.put("status", "error");
                    errorMap.put("message", "Invalid Google token");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
                }
            }else{
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                email = ((UserDetails)principal).getUsername();
                User user = userRepository.findByEmail(email).orElseThrow();
                name = user.getName();
            }

            List<ReferredCandidate> referredCandidates = referredCandidateRepository.findByReferrerEmail(email);

            ReferralTallyDTO referralTallyDTO = updateTally(referredCandidates);

            referralTallyDTO.setName(name);

            Map<String,Object> responseMap = new HashMap<>();

            responseMap.put("Tally", referralTallyDTO);

            return ResponseEntity.ok(responseMap);
        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    private ReferralTallyDTO updateTally(List<ReferredCandidate> referredCandidates) {
        ReferralTallyDTO referralTallyDTO = new ReferralTallyDTO();
        int totalReferrals = 0;
        int select = 0;
        int reject = 0;
        int inProgress = 0;

        totalReferrals += referredCandidates.size();

        for (ReferredCandidate candidate : referredCandidates) {
            if (candidate.getInterviewStatus() != null) {

                switch (candidate.getInterviewStatus().getCurrentStatus()) {
                    case "SELECT":
                        select++;
                        break;
                    case "REJECT":
                        reject++;
                        break;
                    default:
                        inProgress++;
                        break;
                }
            } else {
                // If interviewStatus is null, consider it as inProgress
                inProgress++;
            }
        }

        referralTallyDTO.setTotalReferrals(totalReferrals);
        referralTallyDTO.setSelect(select);
        referralTallyDTO.setReject(reject);
        referralTallyDTO.setInProgress(inProgress);

        return referralTallyDTO;
    }
}
