package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GeminiClient extends LLMClient {
    private static final String API_BASE = "https://generativelanguage.googleapis.com/v1/models/";
    private final Gson gson;
    private final String model;
    private int inputTokens = 0;
    private int outputTokens = 0;
    
    public GeminiClient(String name, String apiKey, String model) {
        super(name, apiKey);
        this.gson = new Gson();
        this.model = model;
    }
    
    public int getInputTokens() {
        return inputTokens;
    }
    
    public int getOutputTokens() {
        return outputTokens;
    }
    
    @Override
    public String sendMessage(String message) throws IOException {
        String urlString = API_BASE + model + ":generateContent?key=" + apiKey;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            // Build request body
            JsonObject requestBody = new JsonObject();
            JsonArray contents = new JsonArray();
            JsonObject content = new JsonObject();
            JsonArray parts = new JsonArray();
            JsonObject part = new JsonObject();
            part.addProperty("text", message);
            parts.add(part);
            content.add("parts", parts);
            contents.add(content);
            requestBody.add("contents", contents);
            
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
                    if (jsonResponse.has("usageMetadata")) {
                        JsonObject usage = jsonResponse.getAsJsonObject("usageMetadata");
                        if (usage.has("promptTokenCount")) {
                            inputTokens = usage.get("promptTokenCount").getAsInt();
                        }
                        if (usage.has("candidatesTokenCount")) {
                            outputTokens = usage.get("candidatesTokenCount").getAsInt();
                        }
                    }
                    
                    if (jsonResponse.has("candidates")) {
                        JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
                        if (candidates.size() > 0) {
                            JsonObject candidate = candidates.get(0).getAsJsonObject();
                            JsonObject contentObj = candidate.getAsJsonObject("content");
                            JsonArray partsArray = contentObj.getAsJsonArray("parts");
                            if (partsArray.size() > 0) {
                                markUsed(); // Track usage for load balancing
                                return partsArray.get(0).getAsJsonObject().get("text").getAsString();
                            }
                        }
                    }
                    markUsed(); // Track usage even for empty responses
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
}
