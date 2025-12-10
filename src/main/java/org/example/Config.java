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
        
        String gemini1 = System.getenv("GEMINI_KEY_1");
        if (gemini1 != null) props.setProperty("GEMINI_KEY_1", gemini1);
        
        String gemini2 = System.getenv("GEMINI_KEY_2");
        if (gemini2 != null) props.setProperty("GEMINI_KEY_2", gemini2);
        
        String gemini3 = System.getenv("GEMINI_KEY_3");
        if (gemini3 != null) props.setProperty("GEMINI_KEY_3", gemini3);
        
        String gemini4 = System.getenv("GEMINI_KEY_4");
        if (gemini4 != null) props.setProperty("GEMINI_KEY_4", gemini4);
        
        String gemini5 = System.getenv("GEMINI_KEY_5");
        if (gemini5 != null) props.setProperty("GEMINI_KEY_5", gemini5);
        
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
