package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.config.JwtService;
import com.accolite.EmployeeReferralBackend.models.GoogleTokenPayload;
import com.accolite.EmployeeReferralBackend.models.Role;
import com.accolite.EmployeeReferralBackend.models.User;
import com.accolite.EmployeeReferralBackend.repository.UserRepository;
import com.accolite.EmployeeReferralBackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthServiceImpl implements AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    private final String googleTokenInfoUrl = "https://www.googleapis.com/oauth2/v3/tokeninfo";
    @Override
    public ResponseEntity<Map<String, Object>> saveUser(String googleToken) {
        try {
            // Validate Google token
            String tokenPayload = validateGoogleToken(googleToken);

            if (tokenPayload != null) {
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("status", "success");
                responseMap.put("tokenPayload", tokenPayload);
                return ResponseEntity.ok(responseMap);
            } else {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("status", "error");
                errorMap.put("message", "Invalid Google token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
            }
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("status", "error");
            errorMap.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    private String validateGoogleToken(String googleToken){
        RestTemplate restTemplate = new RestTemplate();
        //  System.out.println(googleToken);
        String tokenInfoUrl = googleTokenInfoUrl + "?id_token=" + googleToken;
        ResponseEntity<GoogleTokenPayload> response = restTemplate.getForEntity(tokenInfoUrl, GoogleTokenPayload.class);

        // System.out.println(response.getBody().getEmail()); To get the email
        String jwtToken;

        if(response.getBody()!=null)
        {
            String email = response.getBody().getEmail();
            User user = userRepository.findByEmail(email)
                    .orElse(null);

            if(user==null){
                User userEntry = new User();
                userEntry.setEmail(response.getBody().getEmail());
                userEntry.setName(response.getBody().getName());
                userEntry.setRole(Role.EMPLOYEE);
                userEntry.setActive(true);
                jwtToken = jwtService.generateToken(userRepository.save(userEntry));
            }else{
                jwtToken = jwtService.generateToken(user);
            }

        }else{
            return null;
        }

        if (response.getStatusCode() == HttpStatus.OK) {
            return jwtToken;
        } else {
            return null;
        }
    }
}
