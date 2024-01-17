package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.models.ResumeData;
import com.accolite.EmployeeReferralBackend.service.GoogleSheetsService;
import com.accolite.EmployeeReferralBackend.utils.NlpProcessor;
import com.accolite.EmployeeReferralBackend.utils.PdfUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PdfController {

    @Autowired
    private GoogleSheetsService googleSheetsService; // Configuration class for Google Sheets API

    @PostMapping("/extractInfo")
    public ResponseEntity<String> extractInfo(@RequestParam("pdfFile") MultipartFile pdfFile) {
        try {

            List<String> blacklistedCompanies = googleSheetsService.readSheet();
//            for (String company:blacklistedCompanies)
//            {
//                System.out.println(company+ " ");
//            }
            String pdfText = PdfUtils.extractTextFromPdf(pdfFile);
            Map<String, Object>  response = NlpProcessor.extractResumeData(pdfText,blacklistedCompanies);

            // Convert ResumeData to JSON
            ObjectMapper objectMapper = new ObjectMapper();
           // String jsonResumeData = objectMapper.writeValueAsString(resumeData);
            String jsonResumeData;

            if(response.containsKey("resumeData")) {

                System.out.println(response.get("resumeData"));
                jsonResumeData = objectMapper.writeValueAsString(response.get("resumeData"));
            }else{
                jsonResumeData = objectMapper.writeValueAsString(response.get("message"));
            }

            return ResponseEntity.ok(jsonResumeData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error extracting information from PDF");
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
