import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ListGeminiModels {
    private static final String API_BASE = "https://generativelanguage.googleapis.com/v1/models";
    private static final Gson gson = new Gson();
    
    // Use first key to list models
    private static final String TEST_KEY = "AIzaSyDFSJrZO5-GBbJLdq8mGnejJuUwIALPec0";
    
    public static void main(String[] args) {
        System.out.println("Listing available Gemini models...");
        System.out.println();
        
        try {
            String urlString = API_BASE + "?key=" + TEST_KEY;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                    
                    if (jsonResponse.has("models")) {
                        JsonArray models = jsonResponse.getAsJsonArray("models");
                        System.out.println("Found " + models.size() + " models:");
                        System.out.println();
                        
                        for (int i = 0; i < models.size(); i++) {
                            JsonObject model = models.get(i).getAsJsonObject();
                            String name = model.get("name").getAsString();
                            String displayName = model.has("displayName") ? model.get("displayName").getAsString() : "N/A";
                            
                            // Extract model name from full path
                            String modelName = name.replace("models/", "");
                            
                            System.out.println("  " + (i+1) + ". " + modelName);
                            System.out.println("     Display: " + displayName);
                            
                            // Check if it supports generateContent
                            if (model.has("supportedGenerationMethods")) {
                                JsonArray methods = model.getAsJsonArray("supportedGenerationMethods");
                                boolean supportsGenerate = false;
                                for (int j = 0; j < methods.size(); j++) {
                                    if (methods.get(j).getAsString().equals("generateContent")) {
                                        supportsGenerate = true;
                                        break;
                                    }
                                }
                                System.out.println("     Generate: " + (supportsGenerate ? "✓ YES" : "✗ NO"));
                            }
                            System.out.println();
                        }
                    } else {
                        System.out.println("No models found in response");
                    }
                }
            } else {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    System.out.println("Error " + responseCode + ": " + errorResponse.toString());
                }
            }
            
            conn.disconnect();
            
        } catch (Exception e) {
            System.out.println("Failed to list models: " + e.getMessage());
            e.printStackTrace();
        }
    }
}