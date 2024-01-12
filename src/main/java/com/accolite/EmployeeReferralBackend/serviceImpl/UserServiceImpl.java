package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.models.GoogleTokenPayload;
import com.accolite.EmployeeReferralBackend.models.User;
import com.accolite.EmployeeReferralBackend.repository.UserRepository;
import com.accolite.EmployeeReferralBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    private final String googleTokenInfoUrl = "https://www.googleapis.com/oauth2/v3/tokeninfo";
    @Override
    public ResponseEntity<Map<String, Object>> getDetailsOfUser(String googleToken) {
        System.out.println(googleToken);
        try{

            RestTemplate restTemplate = new RestTemplate();
            String tokenInfoUrl = googleTokenInfoUrl + "?id_token=" + googleToken;

            ResponseEntity<GoogleTokenPayload> response = restTemplate.getForEntity(tokenInfoUrl, GoogleTokenPayload.class);

            if(response.getBody()!=null)
            {
                String name = response.getBody().getName();
                String email = response.getBody().getEmail();

                User user = userRepository.findByEmail(email).orElseThrow();

                Map<String,Object> responseMap = new HashMap<>();
                responseMap.put("name",name);
                responseMap.put("role", user.getRole());

                return ResponseEntity.ok(responseMap);
            } else {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("status", "error");
                errorMap.put("message", "Invalid Google token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
            }
        }catch (Exception e){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }
}