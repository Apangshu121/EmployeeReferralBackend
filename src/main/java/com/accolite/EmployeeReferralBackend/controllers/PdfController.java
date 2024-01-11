package com.accolite.EmployeeReferralBackend.controllers;

import com.accolite.EmployeeReferralBackend.models.ResumeData;
import com.accolite.EmployeeReferralBackend.utils.NlpProcessor;
import com.accolite.EmployeeReferralBackend.utils.PdfUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class PdfController {

    @PostMapping("/extractInfo")
    public ResponseEntity<String> extractInfo(@RequestParam("pdfFile") MultipartFile pdfFile) {
        try {
            String pdfText = PdfUtils.extractTextFromPdf(pdfFile);
            ResumeData resumeData = NlpProcessor.extractResumeData(pdfText);

            // Convert ResumeData to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResumeData = objectMapper.writeValueAsString(resumeData);

            return ResponseEntity.ok(jsonResumeData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error extracting information from PDF");
        }
    }
}
