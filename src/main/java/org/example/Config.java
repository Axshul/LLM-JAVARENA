package org.example;

import java.io.*;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();
    private static boolean loaded = false;
    
    static {
        loadConfig();
    }
    
    private static void loadConfig() {
        if (loaded) return;
        
        // Try to load from .env file
        File envFile = new File(".env");
        if (envFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        props.setProperty(parts[0].trim(), parts[1].trim());
                    }
                }
                loaded = true;
                return;
            } catch (IOException e) {
                // Fall through to environment variables
            }
        }
        
        // Try environment variables
        String groqKey = System.getenv("GROQ_API_KEY");
        if (groqKey != null) props.setProperty("GROQ_API_KEY", groqKey);
        
        String openRouterKey = System.getenv("OPENROUTER_API_KEY");
        if (openRouterKey != null) props.setProperty("OPENROUTER_API_KEY", openRouterKey);
        
        // Load all 14 Gemini keys
        for (int i = 1; i <= 14; i++) {
            String geminiKey = System.getenv("GEMINI_KEY_" + i);
            if (geminiKey != null) props.setProperty("GEMINI_KEY_" + i, geminiKey);
        }
        
        String n8nUrl = System.getenv("N8N_WEBHOOK_URL");
        if (n8nUrl != null) props.setProperty("N8N_WEBHOOK_URL", n8nUrl);
        
        loaded = true;
    }
    
    public static String get(String key) {
        return props.getProperty(key);
    }
    
    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}
