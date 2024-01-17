package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.models.ResumeData;
import com.accolite.EmployeeReferralBackend.service.GoogleSheetsService;
import com.accolite.EmployeeReferralBackend.utils.NlpProcessor;
import com.accolite.EmployeeReferralBackend.utils.PdfUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PdfController {

    @Autowired
    private GoogleSheetsService googleSheetsService; // Configuration class for Google Sheets API

    @PostMapping("/extractInfo")
    public ResponseEntity<String> extractInfo(@RequestParam("pdfFile") MultipartFile pdfFile) {
        try {
            List<String> blacklistedCompanies = googleSheetsService.readSheet();
//
            blacklistedCompanies.remove(0);
            String pdfText = PdfUtils.extractTextFromPdf(pdfFile);
            System.out.println(pdfText);
            for(String blacklistCompany:blacklistedCompanies)
            {
                if(pdfText.trim().toLowerCase().contains(blacklistCompany.trim().toLowerCase())){
                    String errorResponse = "{\"error\":\"The candidate cannot be referred\"}";
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
                }
            }

            ResumeData resumeData = NlpProcessor.extractResumeData(pdfText);

            // Convert ResumeData to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResumeData = objectMapper.writeValueAsString(resumeData);
            System.out.println(jsonResumeData);
            return ResponseEntity.ok(jsonResumeData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error extracting information from PDF");
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
