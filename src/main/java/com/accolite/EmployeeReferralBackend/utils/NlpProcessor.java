package com.accolite.EmployeeReferralBackend.utils;

import com.accolite.EmployeeReferralBackend.models.ResumeData;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NlpProcessor {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
    private static final Pattern PRIMARY_SKILL_PATTERN = Pattern.compile("\\bPrimary\\s*Skill\\s*:\\s*([A-Za-z+]+)\\b");
    private static final String MODEL_PATH = "en-ner-person.bin";

    public static ResumeData extractResumeData(String text) throws IOException {
        TokenNameFinderModel nameModel = loadModel(MODEL_PATH);
        NameFinderME nameFinder = new NameFinderME(nameModel);

        Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(text);

        Span[] nameSpans = nameFinder.find(tokens);
        String name = "";

        if (nameSpans.length > 0) {
            // Concatenate only the tokens identified as names
            StringBuilder nameBuilder = new StringBuilder();
            for (Span span : nameSpans) {
                for (int i = span.getStart(); i < span.getEnd(); i++) {
                    nameBuilder.append(tokens[i]).append(" ");
                }
            }
            name = nameBuilder.toString().trim();
        } else {
            // Fallback approach: Use a regex to capture common name patterns
            name = extractNameUsingRegex(text);
        }

        // Extract email using regex pattern
        Matcher emailMatcher = EMAIL_PATTERN.matcher(text);
        String email = emailMatcher.find() ? emailMatcher.group() : "";

        // Extract phone number using a simple pattern
        String phonePattern = "\\b\\d{10}\\b";
        Pattern phonePatternRegex = Pattern.compile(phonePattern);
        Matcher phoneMatcher = phonePatternRegex.matcher(text);
        String phone = phoneMatcher.find() ? phoneMatcher.group() : "";

        // Extract experience using a simple pattern
        String experiencePattern = "\\b\\d+\\s?years?\\b";
        Pattern experiencePatternRegex = Pattern.compile(experiencePattern);
        Matcher experienceMatcher = experiencePatternRegex.matcher(text);
        String experience = experienceMatcher.find() ? experienceMatcher.group() : "0";

        String primarySkill = extractPrimarySkill(text);

        return new ResumeData(name, email, phone, experience, primarySkill);
    }
//    private static String extractNameFallback(String[] tokens) {
//        // Fallback approach: Assuming the name is in the first two tokens
//        return tokens.length >= 2 ? String.join(" ", Arrays.copyOfRange(tokens, 0, 2)) : "";
//    }
    private static String extractPrimarySkill(String text) {
        Matcher skillMatcher = PRIMARY_SKILL_PATTERN.matcher(text);
        return skillMatcher.find() ? skillMatcher.group(1) : "";
    }
    private static String extractNameUsingRegex(String text) {
        // Regex pattern to capture common name patterns
        String namePattern = "\\b(?:Mr\\.?|Mrs\\.?|Miss|Ms\\.?)\\s+([A-Za-z]+)\\b";
        Pattern regexPattern = Pattern.compile(namePattern);
        Matcher matcher = regexPattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }
    private static TokenNameFinderModel loadModel(String modelPath) throws IOException
    {
        try (InputStream modelIn = new FileInputStream(modelPath)) {
            return new TokenNameFinderModel(modelIn);
        }
    }
}
