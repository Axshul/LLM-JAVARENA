package org.example;

import java.io.IOException;

public abstract class LLMClient {
    protected String name;
    protected String apiKey;
    
    public LLMClient(String name, String apiKey) {
        this.name = name;
        this.apiKey = apiKey;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract String sendMessage(String message) throws IOException;
    
    public abstract boolean isAvailable();
}
