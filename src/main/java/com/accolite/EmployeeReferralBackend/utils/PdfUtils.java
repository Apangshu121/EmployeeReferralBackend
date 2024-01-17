package com.accolite.EmployeeReferralBackend.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class PdfUtils {

    public static String extractTextFromPdf(MultipartFile pdfFile) throws IOException {
        try (InputStream inputStream = pdfFile.getInputStream()) {
            PDDocument document = PDDocument.load(inputStream);
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String pdfText = pdfTextStripper.getText(document);

            document.close();

            return pdfText;
        }
    }
}
