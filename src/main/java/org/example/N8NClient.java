package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class N8NClient extends LLMClient {
    private final Gson gson;
    
    public N8NClient(String name, String webhookUrl) {
        super(name, webhookUrl); // Using apiKey field to store webhook URL
        this.gson = new Gson();
    }
    
    @Override
    public String sendMessage(String message) throws IOException {
        URL url = new URL(apiKey); // apiKey contains webhook URL
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            // Build request body
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("message", message);
            requestBody.addProperty("timestamp", System.currentTimeMillis());
            
            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Read response
            int responseCode = conn.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    // Try to parse as JSON, otherwise return raw response
                    try {
                        JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                        if (jsonResponse.has("response")) {
                            return jsonResponse.get("response").getAsString();
                        } else if (jsonResponse.has("message")) {
                            return jsonResponse.get("message").getAsString();
                        }
                        return response.toString();
                    } catch (Exception e) {
                        return response.toString();
                    }
                }
            } else {
                throw new IOException("n8n Webhook Error (" + responseCode + ")");
            }
        } finally {
            conn.disconnect();
        }
    }
    
    @Override
    public boolean isAvailable() {
        return apiKey != null && !apiKey.isEmpty() && apiKey.startsWith("http");
    }
}
