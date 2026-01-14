package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        CLIRenderer.initialize();
        CLIRenderer.printBanner();
        
        // Initialize LLM Council
        LLMCouncil council = new LLMCouncil();
        ConversationManager conversationManager = new ConversationManager();
        
        // Initialize models silently
        addGroqModels(council);
        addOpenRouterModels(council);
        addGeminiModels(council);
        
        if (council.getMemberCount() == 0) {
            CLIRenderer.printError("❌ No AI models available! Check your API keys.");
            return;
        }
        
        // Perform health checks
        council.performHealthChecks();
        
        int healthyCount = council.getHealthyMemberCount();
        if (healthyCount == 0) {
            CLIRenderer.printError("❌ No healthy models found! Check your API keys.");
            return;
        }
        CLIRenderer.printSystemMessage("Type 'help' for available commands");
        System.out.println();
        
        // Main CLI loop
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            boolean running = true;
            
            while (running) {
                CLIRenderer.printPrompt();
                String input = reader.readLine();
                CLIRenderer.printPromptEnd();
                
                if (input == null || input.trim().isEmpty()) {
                    continue;
                }
                
                input = input.trim();
                conversationManager.addUserMessage(input);
                
                // Handle commands
                if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                    CLIRenderer.printSystemMessage("Shutting down AI Council...");
                    running = false;
                    
                } else if (input.equalsIgnoreCase("help")) {
                    CLIRenderer.printHelp();
                    
                } else if (input.equalsIgnoreCase("/clear")) {
                    CLIRenderer.clear();
                    CLIRenderer.printBanner();
                    
                } else if (input.equalsIgnoreCase("/history")) {
                    conversationManager.printHistory();
                    
                } else if (input.equalsIgnoreCase("/models")) {
                    printModelStatus(council);
                    
                } else if (input.equalsIgnoreCase("/tokens")) {
                    printTokenStats(council, conversationManager);
                    
                } else if (input.startsWith("/council ")) {
                    String message = input.substring(9).trim();
                    if (!message.isEmpty()) {
                        council.askCouncil(message, conversationManager);
                    } else {
                        CLIRenderer.printError("Please provide a message for the council");
                        CLIRenderer.printSystemMessage("Usage: /council <your message>");
                    }
                    
                } else if (input.startsWith("/ask ")) {
                    String[] parts = input.substring(5).trim().split(" ", 2);
                    if (parts.length == 2) {
                        String modelName = parts[0];
                        String message = parts[1];
                        council.askSingle(modelName, message, conversationManager);
                    } else {
                        CLIRenderer.printError("Usage: /ask <model-name> <message>");
                        CLIRenderer.printSystemMessage("Example: /ask groq-1 hello");
                    }
                    
                } else if (input.startsWith("/")) {
                    CLIRenderer.printError("Unknown command: " + input);
                    CLIRenderer.printSystemMessage("Type 'help' to see available commands");
                    
                } else {
                    // Default: ask the first available model
                    LLMClient firstModel = council.getFirstAvailable();
                    if (firstModel != null) {
                        council.askSingle(firstModel.getName(), input, conversationManager);
                    } else {
                        CLIRenderer.printError("No models available!");
                    }
                }
            }
            
        } catch (Exception e) {
            CLIRenderer.printError("Fatal error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            council.shutdown();
            CLIRenderer.shutdown();
        }
        
        CLIRenderer.printSuccess("Goodbye! Thanks for using AI Council Arena!");
    }
    
    private static void addGroqModels(LLMCouncil council) {
        String groqApiKey = Config.get("GROQ_API_KEY", "");
        if (groqApiKey.isEmpty()) return;

        String[][] groqModels = {
            {"groq-llama-3.3-70b", "llama-3.3-70b-versatile"},
            {"groq-llama-3.1-8b", "llama-3.1-8b-instant"},
            {"groq-mixtral-8x7b", "mixtral-8x7b-32768"}
        };
        
        for (String[] model : groqModels) {
            try {
                council.addMember(new GroqClient(model[0], groqApiKey, model[1]));
            } catch (Exception e) {
                // Silent failure
            }
        }
    }
    
    private static void addOpenRouterModels(LLMCouncil council) {
        String openRouterApiKey = Config.get("OPENROUTER_API_KEY", "");
        if (openRouterApiKey.isEmpty()) return;

        String[][] openRouterModels = {
            {"openrouter-deepseek", "deepseek/deepseek-chat"},
            {"openrouter-llama-3.1-8b", "meta-llama/llama-3.1-8b-instruct:free"},
            {"openrouter-mistral-7b", "mistralai/mistral-7b-instruct:free"},
            {"openrouter-phi-3", "microsoft/phi-3-mini-128k-instruct:free"}
        };
        
        for (String[] model : openRouterModels) {
            try {
                council.addMember(new OpenRouterClient(model[0], openRouterApiKey, model[1]));
            } catch (Exception e) {
                // Silent failure
            }
        }
    }
    
    private static void addGeminiModels(LLMCouncil council) {
        // Gemini models - using gemini-2.5-flash-lite for speed and efficiency
        String model = "gemini-2.5-flash-lite";
        
        for (int i = 1; i <= 14; i++) {
            String key = Config.get("GEMINI_KEY_" + i, "");
            if (key != null && !key.isEmpty()) {
                try {
                    String name = "gemini-" + i;
                    council.addMember(new GeminiClient(name, key, model));
                } catch (Exception e) {
                    // Silent failure
                }
            }
        }
    }
    
    private static void printModelStatus(LLMCouncil council) {
        CLIRenderer.printFlaminHeader("🔥 FLAMIN' MODEL STATUS 🔥");
        
        List<LLMClient> members = council.getAllMembers();
        int healthyCount = council.getHealthyMemberCount();
        
        CLIRenderer.printFlaminSuccess("Total Models: " + members.size() + " | Healthy: " + healthyCount + " | Load Balanced: YES");
        System.out.println();
        
        for (LLMClient client : members) {
            String provider = "Unknown";
            String modelId = "N/A";
            
            if (client instanceof GroqClient) {
                provider = "Groq";
                modelId = "Lightning Fast ⚡";
            } else if (client instanceof OpenRouterClient) {
                provider = "OpenRouter";
                modelId = "Diverse & Free 🌟";
            } else if (client instanceof GeminiClient) {
                provider = "Gemini";
                modelId = "Google Power 🚀";
            }
            
            // Show health status instead of just availability
            boolean isHealthy = client.isHealthy();
            CLIRenderer.printModelStatus(client.getName(), provider, modelId, isHealthy);
            
            // Show usage stats
            if (client.getUsageCount() > 0) {
                System.out.println("    📊 Usage: " + client.getUsageCount() + " requests | Last used: " + 
                    (System.currentTimeMillis() - client.getLastUsed()) / 1000 + "s ago");
            }
        }
        
        System.out.println();
        CLIRenderer.printSeparator();
    }
    
    private static final String BOLD = "\u001B[1m";
    private static final String RESET = "\u001B[0m";
    
    private static void printTokenStats(LLMCouncil council, ConversationManager conversationManager) {
        System.out.println();
        CLIRenderer.printSeparator();
        System.out.println(BOLD + "  TOKEN USAGE STATISTICS:" + RESET);
        System.out.println();
        System.out.println("    Total Input Tokens:  " + council.getTotalInputTokens());
        System.out.println("    Total Output Tokens: " + council.getTotalOutputTokens());
        System.out.println("    Total Tokens:        " + (council.getTotalInputTokens() + council.getTotalOutputTokens()));
        System.out.println();
        System.out.println("    Messages in History: " + conversationManager.getMessageCount());
        System.out.println();
        CLIRenderer.printSeparator();
    }
}
