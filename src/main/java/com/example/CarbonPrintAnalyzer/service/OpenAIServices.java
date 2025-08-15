package com.example.CarbonPrintAnalyzer.service;

import com.example.CarbonPrintAnalyzer.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class OpenAIServices {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String OPENAI_API_KEY = "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";

    public Response analyzeImageWithBase64(String base64DataUri) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");

        Map<String, Object> textPart = Map.of(
                "type", "text",
                "text", "You are a vision and food analysis expert. Look at the provided image and identify the dish. Then, detect all the main ingredients used in the dish. For each ingredient, estimate its carbon footprint in kilograms (kg CO2e) based on typical values. Finally, return the result strictly in JSON format: {\"dish\": \"<Dish Name>\", \"estimated_carbon_kg\": <Total>, \"ingredients\": [{\"name\": \"<Ingredient>\", \"carbon_kg\": <value>}]}. Respond ONLY in JSON. If unsure, return {\"error\": \"Could not confidently identify dish or ingredients from the image\"}"
        );

        Map<String, Object> imagePart = Map.of(
                "type", "image_url",
                "image_url", Map.of("url", base64DataUri)
        );

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", List.of(textPart, imagePart)
        );

        requestBody.put("messages", List.of(message));
        requestBody.put("max_tokens", 500);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPENAI_API_KEY);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                OPENAI_API_URL, HttpMethod.POST, entity, String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> fullResponse = mapper.readValue(response.getBody(), Map.class);
        String modelOutput = (String) ((Map<String, Object>) ((Map<String, Object>) ((List<?>) fullResponse.get("choices")).get(0)).get("message")).get("content");

        return mapper.readValue(modelOutput, Response.class);
    }

}
