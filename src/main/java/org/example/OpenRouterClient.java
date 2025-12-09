package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class OpenRouterClient extends LLMClient {
    private static final String API_ENDPOINT = "https://openrouter.ai/api/v1/chat/completions";
    private final Gson gson;
    private final String model;
    private int inputTokens = 0;
    private int outputTokens = 0;
    
    public OpenRouterClient(String name, String apiKey, String model) {
        super(name, apiKey);
        this.gson = new Gson();
        this.model = model;
    }
    
    @Override
    public String sendMessage(String message) throws IOException {
        URL url = new URL(API_ENDPOINT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("HTTP-Referer", "https://github.com/llm-council");
            conn.setRequestProperty("X-Title", "LLM Council Arena");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(30000);
            
            // Build request body
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", model);
            
            JsonArray messages = new JsonArray();
            JsonObject userMessage = new JsonObject();
            userMessage.addProperty("role", "user");
            userMessage.addProperty("content", message);
            messages.add(userMessage);
            
            requestBody.add("messages", messages);
            requestBody.addProperty("temperature", 0.7);
            requestBody.addProperty("max_tokens", 1024);
            
            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Read response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    // Parse response
                    JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                    
                    // Extract token usage
                    if (jsonResponse.has("usage")) {
                        JsonObject usage = jsonResponse.getAsJsonObject("usage");
                        if (usage.has("prompt_tokens")) {
                            inputTokens = usage.get("prompt_tokens").getAsInt();
                        }
                        if (usage.has("completion_tokens")) {
                            outputTokens = usage.get("completion_tokens").getAsInt();
                        }
                    }
                    
                    if (jsonResponse.has("choices")) {
                        JsonArray choices = jsonResponse.getAsJsonArray("choices");
                        if (choices.size() > 0) {
                            JsonObject choice = choices.get(0).getAsJsonObject();
                            JsonObject messageObj = choice.getAsJsonObject("message");
                            return messageObj.get("content").getAsString();
                        }
                    }
                    return "No response generated";
                }
            } else {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    throw new IOException("API Error (" + responseCode + "): " + errorResponse.toString());
                }
            }
        } finally {
            conn.disconnect();
        }
    }
    
    @Override
    public boolean isAvailable() {
        return apiKey != null && !apiKey.isEmpty();
    }
    
    public int getInputTokens() {
        return inputTokens;
    }
    
    public int getOutputTokens() {
        return outputTokens;
    }
}
