package com.example.CarbonPrintAnalyzer.service;


import com.example.CarbonPrintAnalyzer.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import java.util.Base64;

@Service
public class GeminiService {

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=AIzaSyD27Qyj208bYwn2Rx3kb92aoMg4-O8NaEo";

    private final ObjectMapper mapper = new ObjectMapper();

    public Response analyzeMultipartImage(MultipartFile file) throws Exception {
        byte[] imageBytes = file.getBytes();
        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        String dataUri = "data:" + file.getContentType() + ";base64," + base64;

        // Prompt that forces format
        String prompt =
                "You are a food vision expert. Identify the dish in the image. Then list its main ingredients and estimate the carbon footprint (kg CO2e) for each ingredient and the total. " +
                        "Return ONLY the JSON exactly in this format: " +
                        "{ \"dish\": \"<Dish Name>\", \"estimated_carbon_kg\": <Total>, \"ingredients\": [ {\"name\": \"<Ingredient>\", \"carbon_kg\": <value>} ] }. " +
                        "If unsure respond with only: {\"error\":\"Could not confidently identify dish or ingredients from the image\"}";

        // Build Gemini request
        Map<String, Object> imageObj = Map.of(
                "mimeType", file.getContentType(),
                "data", base64
        );

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt),
                                        Map.of("inlineData", imageObj)
                                )
                        )
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(GEMINI_API_URL, HttpMethod.POST, entity, String.class);

        // Gemini wraps the JSON in "candidates[0].content.parts[0].text"
        // Gemini wraps the JSON in "candidates[0].content.parts[0].text"
        Map<String, Object> full = mapper.readValue(response.getBody(), Map.class);
        Map<String, Object> message =
                (Map<String, Object>) ((Map<?, ?>) ((List<?>) full.get("candidates")).get(0)).get("content");
        List<?> parts = (List<?>) message.get("parts");
        String modelJson = (String) ((Map<?, ?>) parts.get(0)).get("text");

// ----------- CLEAN THE RESPONSE -----------------
        modelJson = modelJson.trim();
        if (modelJson.startsWith("```")) {
            int start = modelJson.indexOf("{");
            int end = modelJson.lastIndexOf("}");
            if (start != -1 && end != -1) {
                modelJson = modelJson.substring(start, end + 1);
            }
        }
// -----------------------------------------------

// Parse JSON into DishResponse
        return mapper.readValue(modelJson, Response.class);

    }


    public Response analyzeTextOnly(String userText) throws Exception {
        // Build a prompt
        String prompt =
                "You are a food expert. Based only on the following user text: \"" + userText + "\" " +
                        "identify the dish, list main ingredients and estimate the carbon footprint (kg CO2e) for each ingredient and the total. " +
                        "Return ONLY the JSON exactly in this format: " +
                        "{ \"dish\": \"<Dish Name>\", \"estimated_carbon_kg\": <Total>, \"ingredients\": [ {\"name\": \"<Ingredient>\", \"carbon_kg\": <value>} ] }. " +
                        "If unsure respond with only: {\"error\":\"Could not confidently identify dish or ingredients\"}";

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(GEMINI_API_URL, HttpMethod.POST, entity, String.class);

        // Gemini wraps the JSON in "candidates[0].content.parts[0].text"
        Map<String, Object> full = mapper.readValue(response.getBody(), Map.class);
        List<?> candidates = (List<?>) full.get("candidates");
        Map<String, Object> message = (Map<String, Object>) ((Map<?, ?>) candidates.get(0)).get("content");
        List<?> parts = (List<?>) message.get("parts");
        String modelJson = (String) ((Map<?, ?>) parts.get(0)).get("text");

        // Clean
        modelJson = modelJson.trim();
        if (modelJson.startsWith("```")) {
            int start = modelJson.indexOf("{");
            int end = modelJson.lastIndexOf("}");
            if (start != -1 && end != -1) {
                modelJson = modelJson.substring(start, end + 1);
            }
        }

        return mapper.readValue(modelJson, Response.class);
    }

}

