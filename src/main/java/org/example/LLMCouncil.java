package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class LLMCouncil {
    private final List<LLMClient> members;
    private final ExecutorService executor;
    private final FallbackClient ultimateFallback;
    private LLMClient chairman;
    private int totalInputTokens = 0;
    private int totalOutputTokens = 0;
    private int roundRobinIndex = 0; // For proper round-robin load balancing
    
    public LLMCouncil() {
        this.members = new ArrayList<>();
        this.executor = Executors.newFixedThreadPool(12); // Increased for more Gemini models
        this.ultimateFallback = new FallbackClient();
    }
    
    // Perform health checks on all members (streamlined)
    public void performHealthChecks() {
        CLIRenderer.printProgress("🔍 Health checking " + members.size() + " models");
        
        List<Future<Boolean>> healthFutures = new ArrayList<>();
        
        for (LLMClient client : members) {
            healthFutures.add(executor.submit(() -> {
                boolean healthy = client.performHealthCheck();
                if (healthy) {
                    CLIRenderer.printDot(); // Just show progress dots
                }
                return healthy;
            }));
        }
        
        // Wait for all health checks to complete
        for (Future<Boolean> future : healthFutures) {
            try {
                future.get(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                // Health check failed
            }
        }
        
        int healthyCount = getHealthyMemberCount();
        CLIRenderer.printProgressDone();
        CLIRenderer.printSuccess("🚀 " + healthyCount + "/" + members.size() + " models ready | Load balancing: ON");
    }
    
    // Get count of healthy members
    public int getHealthyMemberCount() {
        return (int) members.stream().filter(LLMClient::isHealthy).count();
    }
    
    // Get least used healthy model for load balancing
    public LLMClient getLeastUsedHealthyModel() {
        return members.stream()
                .filter(LLMClient::isHealthy)
                .min((a, b) -> Integer.compare(a.getUsageCount(), b.getUsageCount()))
                .orElse(null);
    }
    
    // Get least recently used healthy model
    public LLMClient getLeastRecentlyUsedHealthyModel() {
        return members.stream()
                .filter(LLMClient::isHealthy)
                .min((a, b) -> Long.compare(a.getLastUsed(), b.getLastUsed()))
                .orElse(null);
    }
    
    public void addMember(LLMClient client) {
        if (client.isAvailable()) {
            members.add(client);
            // First Groq model becomes chairman
            if (chairman == null && client.getName().toLowerCase().contains("groq")) {
                chairman = client;
            }
        }
    }
    
    public LLMClient getMember(String name) {
        for (LLMClient client : members) {
            if (client.getName().equalsIgnoreCase(name)) {
                return client;
            }
        }
        return null;
    }
    
    public List<LLMClient> getAllMembers() {
        return new ArrayList<>(members);
    }
    
    public LLMClient getFirstAvailable() {
        // Get healthy models only
        List<LLMClient> healthyModels = members.stream()
                .filter(LLMClient::isHealthy)
                .collect(java.util.stream.Collectors.toList());
        
        if (healthyModels.isEmpty()) {
            // Fallback to any available model
            for (LLMClient client : members) {
                if (client.isAvailable()) {
                    return client;
                }
            }
            return null;
        }
        
        // Round-robin through healthy models
        LLMClient selected = healthyModels.get(roundRobinIndex % healthyModels.size());
        roundRobinIndex = (roundRobinIndex + 1) % healthyModels.size();
        
        return selected;
    }
    
    public void askCouncil(String message, ConversationManager conversationManager) {
        if (members.isEmpty()) {
            CLIRenderer.printWarning("No AI models available - using ultimate fallback");
            tryUltimateFallback(message, conversationManager);
            return;
        }
        
        CLIRenderer.printCouncilHeader(members.size());
        
        List<Future<CouncilResponse>> futures = new ArrayList<>();
        List<CouncilResponse> successfulResponses = new ArrayList<>();
        
        // Submit all requests concurrently
        int index = 1;
        for (LLMClient client : members) {
            CLIRenderer.printModelQuerying(client.getName(), index++, members.size());
            futures.add(executor.submit(() -> {
                try {
                    String response = client.sendMessage(message);
                    int inTokens = 0, outTokens = 0;
                    if (client instanceof GeminiClient) {
                        GeminiClient gc = (GeminiClient) client;
                        inTokens = gc.getInputTokens();
                        outTokens = gc.getOutputTokens();
                    } else if (client instanceof GroqClient) {
                        GroqClient gc = (GroqClient) client;
                        inTokens = gc.getInputTokens();
                        outTokens = gc.getOutputTokens();
                    } else if (client instanceof OpenRouterClient) {
                        OpenRouterClient gc = (OpenRouterClient) client;
                        inTokens = gc.getInputTokens();
                        outTokens = gc.getOutputTokens();
                    }
                    return new CouncilResponse(client.getName(), response, null, inTokens, outTokens);
                } catch (Exception e) {
                    return new CouncilResponse(client.getName(), null, "DIDN'T WORK", 0, 0);
                }
            }));
        }
        
        // Collect responses - only show successful ones
        System.out.println();
        for (Future<CouncilResponse> future : futures) {
            try {
                CouncilResponse response = future.get(45, TimeUnit.SECONDS);
                if (response.error == null && response.message != null) {
                    successfulResponses.add(response);
                    String color = CLIRenderer.getColorForLLM(response.llmName);
                    CLIRenderer.printLLMResponse(response.llmName, response.message, color);
                    if (response.inputTokens > 0) {
                        CLIRenderer.printTokenInfo(response.llmName, response.inputTokens, response.outputTokens);
                        totalInputTokens += response.inputTokens;
                        totalOutputTokens += response.outputTokens;
                    }
                }
            } catch (TimeoutException e) {
                // Silently skip timeout
            } catch (Exception e) {
                // Silently skip errors
            }
        }
        
        if (successfulResponses.isEmpty()) {
            CLIRenderer.printWarning("All models failed - using ultimate fallback");
            tryUltimateFallback(message, conversationManager);
            return;
        }
        
        // Chairman evaluates and picks the best response
        if (chairman != null && successfulResponses.size() > 1) {
            System.out.println();
            CLIRenderer.printSystemMessage("Chairman is evaluating responses...");
            String bestResponse = evaluateResponses(message, successfulResponses);
            if (bestResponse != null) {
                conversationManager.addLLMMessage(bestResponse, "Council-Best");
            } else {
                // Add first successful response if evaluation fails
                conversationManager.addLLMMessage(successfulResponses.get(0).message, successfulResponses.get(0).llmName);
            }
        } else if (!successfulResponses.isEmpty()) {
            conversationManager.addLLMMessage(successfulResponses.get(0).message, successfulResponses.get(0).llmName);
        }
        
        CLIRenderer.printCouncilFooter(successfulResponses.size(), members.size());
    }
    
    private String evaluateResponses(String originalQuestion, List<CouncilResponse> responses) {
        try {
            StringBuilder evaluation = new StringBuilder();
            evaluation.append("Question: ").append(originalQuestion).append("\n\n");
            evaluation.append("Evaluate these responses and return ONLY the number (1-").append(responses.size()).append(") of the best response:\n\n");
            
            for (int i = 0; i < responses.size(); i++) {
                evaluation.append((i + 1)).append(". ").append(responses.get(i).message).append("\n\n");
            }
            
            evaluation.append("Return only the number of the best response:");
            
            String chairmanDecision = chairman.sendMessage(evaluation.toString());
            
            // Extract number from chairman's response
            String numberStr = chairmanDecision.replaceAll("[^0-9]", "");
            if (!numberStr.isEmpty()) {
                int bestIndex = Integer.parseInt(numberStr) - 1;
                if (bestIndex >= 0 && bestIndex < responses.size()) {
                    CLIRenderer.printSuccess("Chairman selected response #" + (bestIndex + 1) + " from " + responses.get(bestIndex).llmName);
                    return responses.get(bestIndex).message;
                }
            }
        } catch (Exception e) {
            // If evaluation fails, return first response
        }
        return null;
    }
    
    private void tryUltimateFallback(String message, ConversationManager conversationManager) {
        try {
            CLIRenderer.printSystemMessage("Contacting ultimate fallback system...");
            String response = ultimateFallback.sendMessage(message);
            String color = CLIRenderer.getColorForLLM("fallback");
            CLIRenderer.printLLMResponse("Ultimate-Fallback", response, color);
            conversationManager.addLLMMessage(response, "Ultimate-Fallback");
        } catch (Exception e) {
            CLIRenderer.printError("Ultimate fallback also failed. Please check your internet connection.");
        }
    }
    
    public void askSingle(String llmName, String message, ConversationManager conversationManager) {
        LLMClient client = getMember(llmName);
        if (client == null) {
            // Use load balancer
            client = getFirstAvailable();
            if (client == null) {
                CLIRenderer.printFlaminWarning("No healthy models available - using ultimate fallback");
                tryUltimateFallback(message, conversationManager);
                return;
            }
            // Show load balancing info
            CLIRenderer.printLoadBalanceInfo(client.getName(), client.getUsageCount());
        }
        
        try {
            CLIRenderer.printThinking(client.getName());
            for (int i = 0; i < 10; i++) {
                Thread.sleep(200);
                CLIRenderer.printDot();
            }
            CLIRenderer.printThinkingDone();
            
            String response = client.sendMessage(message);
            String color = CLIRenderer.getColorForLLM(client.getName());
            CLIRenderer.printLLMResponse(client.getName(), response, color);
            
            // Show token info
            int inTokens = 0, outTokens = 0;
            if (client instanceof GeminiClient) {
                GeminiClient gc = (GeminiClient) client;
                inTokens = gc.getInputTokens();
                outTokens = gc.getOutputTokens();
            } else if (client instanceof GroqClient) {
                GroqClient gc = (GroqClient) client;
                inTokens = gc.getInputTokens();
                outTokens = gc.getOutputTokens();
            } else if (client instanceof OpenRouterClient) {
                OpenRouterClient gc = (OpenRouterClient) client;
                inTokens = gc.getInputTokens();
                outTokens = gc.getOutputTokens();
            }
            
            if (inTokens > 0) {
                CLIRenderer.printTokenInfo(client.getName(), inTokens, outTokens);
                totalInputTokens += inTokens;
                totalOutputTokens += outTokens;
            }
            
            conversationManager.addLLMMessage(response, client.getName());
        } catch (Exception e) {
            CLIRenderer.printWarning(client.getName() + " didn't work - trying fallback");
            
            // Try another model
            LLMClient fallback = getFirstAvailableExcept(client.getName());
            if (fallback != null) {
                try {
                    CLIRenderer.printThinking(fallback.getName());
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(200);
                        CLIRenderer.printDot();
                    }
                    CLIRenderer.printThinkingDone();
                    
                    String response = fallback.sendMessage(message);
                    String color = CLIRenderer.getColorForLLM(fallback.getName());
                    CLIRenderer.printLLMResponse(fallback.getName(), response, color);
                    
                    int inTokens = 0, outTokens = 0;
                    if (fallback instanceof GeminiClient) {
                        GeminiClient gc = (GeminiClient) fallback;
                        inTokens = gc.getInputTokens();
                        outTokens = gc.getOutputTokens();
                    } else if (fallback instanceof GroqClient) {
                        GroqClient gc = (GroqClient) fallback;
                        inTokens = gc.getInputTokens();
                        outTokens = gc.getOutputTokens();
                    } else if (fallback instanceof OpenRouterClient) {
                        OpenRouterClient gc = (OpenRouterClient) fallback;
                        inTokens = gc.getInputTokens();
                        outTokens = gc.getOutputTokens();
                    }
                    
                    if (inTokens > 0) {
                        CLIRenderer.printTokenInfo(fallback.getName(), inTokens, outTokens);
                        totalInputTokens += inTokens;
                        totalOutputTokens += outTokens;
                    }
                    
                    conversationManager.addLLMMessage(response, fallback.getName());
                } catch (Exception e2) {
                    CLIRenderer.printWarning("All models failed - using ultimate fallback");
                    tryUltimateFallback(message, conversationManager);
                }
            } else {
                CLIRenderer.printWarning("No fallback models - using ultimate fallback");
                tryUltimateFallback(message, conversationManager);
            }
        }
    }
    
    private LLMClient getFirstAvailableExcept(String exceptName) {
        for (LLMClient client : members) {
            if (client.isAvailable() && !client.getName().equalsIgnoreCase(exceptName)) {
                return client;
            }
        }
        return null;
    }
    
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
    
    public int getMemberCount() {
        return members.size();
    }
    
    public int getTotalInputTokens() {
        return totalInputTokens;
    }
    
    public int getTotalOutputTokens() {
        return totalOutputTokens;
    }
    
    private static class CouncilResponse {
        String llmName;
        String message;
        String error;
        int inputTokens;
        int outputTokens;
        
        CouncilResponse(String llmName, String message, String error, int inputTokens, int outputTokens) {
            this.llmName = llmName;
            this.message = message;
            this.error = error;
            this.inputTokens = inputTokens;
            this.outputTokens = outputTokens;
        }
    }
}
