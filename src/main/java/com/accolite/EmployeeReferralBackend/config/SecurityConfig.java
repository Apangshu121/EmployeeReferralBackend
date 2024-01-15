package com.accolite.EmployeeReferralBackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    AuthenticationProvider authenticationProvider;

    private static final String[] WHITE_LIST_URL = {"/saveUser", "/api/extractInfo"};
    private static final String[] AUTHENTICATED_LIST_URL = {"/getUserDetails", "/api/referredCandidates/add","/api/referredCandidates/getAllCandidatesOfUser"};
    private static final String[] RECRUITER_LIST_URL = {"/api/referredCandidates/getAll",
            "/api/referredCandidates/get/**",
            "/api/referredCandidates/update/**",
            "/api/selectedReferredCandidates/getAll",
            "/api/selectedReferredCandidates/get/**",
            "/api/selectedReferredCandidates/update/**",
            "/api/referredCandidates/filterByExperience/**",
            "/api/referredCandidates/filterByPreferredLocation/**",
            "/api/referredCandidates/filterByNoticePeriod/**",
            "/api/referredCandidates/sendMail/**"

    };

    private static final String[] ADMIN_LIST_URL = {"/admin/users/modify/**","/admin/users/all"};


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(req->req.requestMatchers(WHITE_LIST_URL)
                        .permitAll()
                        .requestMatchers(AUTHENTICATED_LIST_URL).hasAnyAuthority("EMPLOYEE","RECRUITER","BU_HEAD","ADMIN")
                        .requestMatchers(RECRUITER_LIST_URL).hasAuthority("RECRUITER")
                        .requestMatchers(ADMIN_LIST_URL).hasAuthority("ADMIN")
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
