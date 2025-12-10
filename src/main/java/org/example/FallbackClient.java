package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FallbackClient extends LLMClient {
    private static final String BASE_URL = Config.get("N8N_WEBHOOK_URL", 
        "");
    private final Gson gson;
    
    public FallbackClient() {
        super("Ultimate-Fallback", "");
        this.gson = new Gson();
    }
    
    @Override
    public String sendMessage(String message) throws IOException {
        try {
            String encodedPrompt = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
            String urlString = BASE_URL + "?prompt=" + encodedPrompt;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            try {
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(30000);
                
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        
                        // Parse JSON response
                        JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                        if (jsonResponse.has("responce")) {
                            return jsonResponse.get("responce").getAsString();
                        } else if (jsonResponse.has("response")) {
                            return jsonResponse.get("response").getAsString();
                        }
                        return response.toString();
                    }
                } else {
                    throw new IOException("Fallback API returned: " + responseCode);
                }
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            throw new IOException("Fallback failed: " + e.getMessage());
        }
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
}
