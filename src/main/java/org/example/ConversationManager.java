package org.example;

import java.util.ArrayList;
import java.util.List;

public class ConversationManager {
    private static class Message {
        String role;
        String content;
        String llmName;
        long timestamp;
        
        Message(String role, String content, String llmName) {
            this.role = role;
            this.content = content;
            this.llmName = llmName;
            this.timestamp = System.currentTimeMillis();
        }
    }
    
    private final List<Message> history;
    
    public ConversationManager() {
        this.history = new ArrayList<>();
    }
    
    public void addUserMessage(String content) {
        history.add(new Message("user", content, "User"));
    }
    
    public void addLLMMessage(String content, String llmName) {
        history.add(new Message("assistant", content, llmName));
    }
    
    public void printHistory() {
        if (history.isEmpty()) {
            CLIRenderer.printSystemMessage("No conversation history yet.");
            return;
        }
        
        CLIRenderer.printSeparator();
        System.out.println("📜 Conversation History:");
        CLIRenderer.printSeparator();
        
        for (Message msg : history) {
            if (msg.role.equals("user")) {
                CLIRenderer.printUserMessage(msg.content);
            } else {
                String color = getColorForLLM(msg.llmName);
                CLIRenderer.printLLMResponse(msg.llmName, msg.content, color);
            }
        }
        CLIRenderer.printSeparator();
    }
    
    public void clear() {
        history.clear();
        CLIRenderer.printSuccess("Conversation history cleared.");
    }
    
    public int getMessageCount() {
        return history.size();
    }
    
    private String getColorForLLM(String llmName) {
        return CLIRenderer.getColorForLLM(llmName);
    }
}
