package com.snakeporium_backend.services.customer;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiChatServiceImpl {

    @Value("${openai.api.key}") // Load your API key from application.properties or application.yml
    private String apiKey;

    private final String OPENAI_API_URL = "https://api.openai.com/v1/engines/davinci-codex/completions";

    private final RestTemplate restTemplate = new RestTemplate();

    public String getResponse(String question) {
        String requestBody = "{\"prompt\": \"" + question + "\"}";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Make request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(OPENAI_API_URL, HttpMethod.POST, entity, String.class);

        // Return response
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException("Failed to get response from OpenAI API: " + responseEntity.getStatusCode());
        }
    }
}

