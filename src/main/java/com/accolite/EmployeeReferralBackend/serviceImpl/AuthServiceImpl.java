package com.accolite.EmployeeReferralBackend.serviceImpl;

import com.accolite.EmployeeReferralBackend.config.JwtService;
import com.accolite.EmployeeReferralBackend.models.GoogleTokenPayload;
import com.accolite.EmployeeReferralBackend.models.Role;
import com.accolite.EmployeeReferralBackend.models.User;
import com.accolite.EmployeeReferralBackend.repository.UserRepository;
import com.accolite.EmployeeReferralBackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${googleUrl}")
    private String googleTokenInfoUrl;

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
        System.out.println(tokenInfoUrl);
        ResponseEntity<GoogleTokenPayload> response = restTemplate.getForEntity(tokenInfoUrl, GoogleTokenPayload.class);

        // System.out.println(response.getBody().getEmail()); To get the email
        String jwtToken;

        if(response.getBody()!=null)
        {
            String email = response.getBody().getEmail();
            User user = userRepository.findByEmail(email)
                    .orElse(null);

            if(user!=null){
                jwtToken = jwtService.generateToken(user);
            } else {
                jwtToken = "NO TOKEN";
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
