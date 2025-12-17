package org.example;

import java.io.IOException;

public abstract class LLMClient {
    protected String name;
    protected String apiKey;
    protected boolean healthChecked = false;
    protected boolean isHealthy = false;
    protected long lastUsed = 0;
    protected int usageCount = 0;
    
    public LLMClient(String name, String apiKey) {
        this.name = name;
        this.apiKey = apiKey;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract String sendMessage(String message) throws IOException;
    
    public abstract boolean isAvailable();
    
    // Health check - sends a simple test message
    public boolean performHealthCheck() {
        if (healthChecked) return isHealthy;
        
        try {
            String response = sendMessage("Hi");
            isHealthy = response != null && !response.trim().isEmpty();
            healthChecked = true;
            return isHealthy;
        } catch (Exception e) {
            isHealthy = false;
            healthChecked = true;
            return false;
        }
    }
    
    public boolean isHealthy() {
        return isHealthy;
    }
    
    public void markUsed() {
        lastUsed = System.currentTimeMillis();
        usageCount++;
    }
    
    public long getLastUsed() {
        return lastUsed;
    }
    
    public int getUsageCount() {
        return usageCount;
    }
    
    public void resetHealthCheck() {
        healthChecked = false;
        isHealthy = false;
    }
}
