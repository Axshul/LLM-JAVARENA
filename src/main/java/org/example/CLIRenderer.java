package org.example;

public class CLIRenderer {
    
    // ANSI Color codes
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String WHITE = "\u001B[37m";
    private static final String BOLD = "\u001B[1m";
    private static final String DIM = "\u001B[2m";
    private static final String BG_BLUE = "\u001B[44m";
    private static final String BG_GREEN = "\u001B[42m";
    private static final String BG_CYAN = "\u001B[46m";
    private static final String BG_BLACK = "\u001B[40m";
    private static final String BG_YELLOW = "\u001B[43m";
    private static final String BG_RED = "\u001B[41m";
    private static final String BLACK = "\u001B[30m";
    
    private static int responseCounter = 0;
    
    public static void initialize() {
        try {
            new ProcessBuilder("cmd", "/c", "").inheritIO().start().waitFor();
        } catch (Exception e) {
            // Ignore
        }
    }
    
    public static void shutdown() {
        System.out.println();
    }
    
    public static void printBanner() {
        clear();
        System.out.println();
        System.out.println(CYAN + BOLD + "  ╔══════════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + BOLD + "  ║" + RESET + "                                                                      " + CYAN + BOLD + "║" + RESET);
        System.out.println(CYAN + BOLD + "  ║" + RESET + "              " + RED + BOLD + "🔥 AI COUNCIL ARENA v2.0 🔥" + RESET + "                      " + CYAN + BOLD + "║" + RESET);
        System.out.println(CYAN + BOLD + "  ║" + RESET + "              " + WHITE + "Multi-Provider AI Command Center" + RESET + "              " + CYAN + BOLD + "║" + RESET);
        System.out.println(CYAN + BOLD + "  ║" + RESET + "                                                                      " + CYAN + BOLD + "║" + RESET);
        System.out.println(CYAN + BOLD + "  ╚══════════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println(DIM + "  ⚡ Groq • OpenRouter • 14x Gemini APIs • Load Balanced • Health Monitored" + RESET);
        System.out.println();
    }
    
    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    public static void printUserMessage(String message) {
        System.out.println();
        System.out.println(CYAN + "  +---------------------------------------------------------------------------+" + RESET);
        System.out.println(CYAN + "  |" + RESET + BG_BLUE + WHITE + BOLD + " YOU " + RESET + "                                                                      " + CYAN + "|" + RESET);
        System.out.println(CYAN + "  |" + RESET + "  " + WHITE + wrapText(message, 73) + RESET);
        System.out.println(CYAN + "  +---------------------------------------------------------------------------+" + RESET);
    }
    
    public static void printLLMResponse(String llmName, String message, String color) {
        responseCounter++;
        System.out.println();
        System.out.println(color + "  +---------------------------------------------------------------------------+" + RESET);
        System.out.println(color + "  |" + RESET + color + BOLD + " [" + responseCounter + "] " + llmName.toUpperCase() + RESET + color + " " + getProviderEmoji(llmName) + RESET);
        System.out.println(color + "  +---------------------------------------------------------------------------+" + RESET);
        
        // Wrap text to fit in box
        String[] lines = wrapTextMultiLine(message, 73);
        for (String line : lines) {
            System.out.println(color + "  | " + RESET + color + line + RESET);
        }
        
        System.out.println(color + "  +---------------------------------------------------------------------------+" + RESET);
    }
    
    private static String getProviderEmoji(String llmName) {
        if (llmName.toLowerCase().contains("groq")) return "[GROQ]";
        if (llmName.toLowerCase().contains("openrouter")) return "[OPENROUTER]";
        if (llmName.toLowerCase().contains("gemini")) return "[GEMINI]";
        return "[AI]";
    }
    
    private static String wrapText(String text, int width) {
        if (text.length() <= width) {
            return text + " ".repeat(width - text.length());
        }
        return text.substring(0, width - 3) + "...";
    }
    
    private static String[] wrapTextMultiLine(String text, int width) {
        if (text.length() <= width) {
            return new String[]{text};
        }
        
        java.util.List<String> lines = new java.util.ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        
        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= width) {
                if (currentLine.length() > 0) currentLine.append(" ");
                currentLine.append(word);
            } else {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }
        
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        
        return lines.toArray(new String[0]);
    }
    
    public static void printSystemMessage(String message) {
        System.out.println(CYAN + "  [i] " + message + RESET);
    }
    
    public static void printError(String message) {
        System.out.println(RED + "  [!] " + message + RESET);
    }
    
    public static void printSuccess(String message) {
        System.out.println(GREEN + "  [+] " + BOLD + message + RESET);
    }
    
    public static void printWarning(String message) {
        System.out.println(YELLOW + "  [~] " + message + RESET);
    }
    
    public static void printProgress(String message) {
        System.out.print(CYAN + "  [~] " + message + RESET);
        System.out.flush();
    }
    
    public static void printProgressDone() {
        System.out.println(GREEN + " ✓" + RESET);
    }
    
    public static void printThinking(String llmName) {
        System.out.println();
        System.out.println(YELLOW + "  +---------------------------------------------------------------------------+" + RESET);
        System.out.println(YELLOW + "  |" + RESET + BG_YELLOW + BLACK + BOLD + " PROCESSING " + RESET + YELLOW + "                                                             |" + RESET);
        System.out.println(YELLOW + "  |  " + llmName + " is thinking" + RESET);
        System.out.print(YELLOW + "  |  " + RESET);
        System.out.flush();
    }
    
    public static void printDot() {
        System.out.print(YELLOW + "█" + RESET);
        System.out.flush();
    }
    
    public static void printThinkingDone() {
        System.out.println();
        System.out.println(YELLOW + "  +---------------------------------------------------------------------------+" + RESET);
    }
    
    public static void printNewLine() {
        System.out.println();
    }
    
    public static void printTokenInfo(String llmName, int inputTokens, int outputTokens) {
        System.out.println(DIM + "  |  Tokens: " + GREEN + inputTokens + " in" + RESET + DIM + " / " + BLUE + outputTokens + " out" + RESET + DIM + " | Total: " + (inputTokens + outputTokens) + RESET);
    }
    
    public static void printHelp() {
        System.out.println();
        System.out.println(CYAN + "  +---------------------------------------------------------------------------+" + RESET);
        System.out.println(CYAN + "  |" + RESET + BOLD + "                           COMMAND CENTER                                  " + CYAN + "|" + RESET);
        System.out.println(CYAN + "  +---------------------------------------------------------------------------+" + RESET);
        System.out.println();
        System.out.println(GREEN + BOLD + "  CHAT COMMANDS:" + RESET);
        System.out.println("    " + YELLOW + "/council <message>" + RESET + "     Ask all AI models simultaneously");
        System.out.println("    " + YELLOW + "/ask <model> <msg>" + RESET + "     Chat with specific model");
        System.out.println("    " + YELLOW + "<message>" + RESET + "              Quick chat with first available model");
        System.out.println();
        System.out.println(GREEN + BOLD + "  UTILITY COMMANDS:" + RESET);
        System.out.println("    " + YELLOW + "/models" + RESET + "                Show all available models and status");
        System.out.println("    " + YELLOW + "/tokens" + RESET + "                Show token usage statistics");
        System.out.println("    " + YELLOW + "/history" + RESET + "               Show conversation history");
        System.out.println("    " + YELLOW + "/clear" + RESET + "                 Clear the screen");
        System.out.println("    " + YELLOW + "help" + RESET + "                   Show this help menu");
        System.out.println("    " + YELLOW + "exit" + RESET + "                   Exit the application");
        System.out.println();
        System.out.println(CYAN + "  +---------------------------------------------------------------------------+" + RESET);
        System.out.println();
    }
    
    public static void printPrompt() {
        System.out.println();
        System.out.println(CYAN + "  +---------------------------------------------------------------------------+" + RESET);
        System.out.print(CYAN + "  |" + RESET + BOLD + " >> " + RESET);
        System.out.flush();
    }
    
    public static void printPromptEnd() {
        System.out.println();
        System.out.println(CYAN + "  +---------------------------------------------------------------------------+" + RESET);
    }
    
    public static void printSeparator() {
        System.out.println(DIM + "  ---------------------------------------------------------------------------" + RESET);
    }
    
    public static void printModelStatus(String modelName, String provider, String modelId, boolean available) {
        String statusColor = available ? GREEN : RED;
        String statusText = available ? "🔥 BLAZING" : "💀 DEAD";
        String providerBadge = getProviderBadge(provider);
        System.out.println("  " + providerBadge + " " + CYAN + BOLD + modelName + RESET + " " + DIM + "(" + modelId + ")" + RESET + " " + statusColor + BOLD + statusText + RESET);
    }
    
    private static String getProviderBadge(String provider) {
        switch (provider.toLowerCase()) {
            case "groq": return BG_GREEN + BLACK + " GROQ " + RESET;
            case "openrouter": return BG_BLUE + WHITE + " OPENROUTER " + RESET;
            case "gemini": return BG_CYAN + BLACK + " GEMINI " + RESET;
            default: return BG_BLACK + WHITE + " " + provider.toUpperCase() + " " + RESET;
        }
    }
    
    public static String getColorForLLM(String llmName) {
        if (llmName.toLowerCase().contains("groq")) return GREEN;
        if (llmName.toLowerCase().contains("openrouter")) return BLUE;
        if (llmName.toLowerCase().contains("gemini")) return CYAN;
        if (llmName.toLowerCase().contains("llama")) return MAGENTA;
        if (llmName.toLowerCase().contains("mistral")) return YELLOW;
        return WHITE;
    }
    
    public static void printCouncilHeader(int modelCount) {
        responseCounter = 0;
        System.out.println();
        System.out.println(MAGENTA + "  ╔═══════════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(MAGENTA + "  ║" + RESET + BG_CYAN + BLACK + BOLD + "                        COUNCIL SESSION INITIATED                          " + RESET + MAGENTA + "║" + RESET);
        System.out.println(MAGENTA + "  ╠═══════════════════════════════════════════════════════════════════════════╣" + RESET);
        System.out.println(MAGENTA + "  ║" + RESET + "  Consulting " + BOLD + modelCount + " AI Models" + RESET + " in parallel...                                    " + MAGENTA + "║" + RESET);
        System.out.println(MAGENTA + "  ╚═══════════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    public static void printCouncilFooter(int successCount, int totalCount) {
        System.out.println();
        System.out.println(MAGENTA + "  ╔═══════════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(MAGENTA + "  ║" + RESET + BG_GREEN + BLACK + BOLD + "                        COUNCIL SESSION COMPLETE                           " + RESET + MAGENTA + "║" + RESET);
        System.out.println(MAGENTA + "  ╠═══════════════════════════════════════════════════════════════════════════╣" + RESET);
        System.out.println(MAGENTA + "  ║" + RESET + "  " + GREEN + BOLD + successCount + " of " + totalCount + " models" + RESET + " responded successfully                                  " + MAGENTA + "║" + RESET);
        System.out.println(MAGENTA + "  ╚═══════════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    public static void printLoadingAnimation(String message) {
        String[] spinner = {"|", "/", "-", "\\"};
        for (int i = 0; i < 8; i++) {
            System.out.print("\r" + CYAN + "  [" + spinner[i % 4] + "] " + message + RESET);
            System.out.flush();
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.print("\r" + GREEN + "  [✓] " + message + " DONE" + RESET + "\n");
    }
    
    public static void printModelQuerying(String modelName, int index, int total) {
        System.out.println(CYAN + "  [" + index + "/" + total + "] " + RESET + "Querying " + BOLD + modelName + RESET + "...");
    }
    
    // Enhanced methods for FLAMIN' experience
    public static void printFlaminHeader(String title) {
        System.out.println();
        System.out.println(RED + "  🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥" + RESET);
        System.out.println(RED + "  🔥" + RESET + YELLOW + BOLD + "  " + title + RESET + RED + "  🔥" + RESET);
        System.out.println(RED + "  🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥" + RESET);
        System.out.println();
    }
    
    public static void printHealthCheckStart() {
        System.out.println();
        System.out.println(YELLOW + "  ╔═══════════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(YELLOW + "  ║" + RESET + BOLD + "                        🏥 HEALTH CHECK INITIATED 🏥                        " + RESET + YELLOW + "║" + RESET);
        System.out.println(YELLOW + "  ╚═══════════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    public static void printLoadBalanceInfo(String modelName, int usageCount) {
        System.out.println(DIM + "  ⚖️  Load balancer → " + RESET + BOLD + modelName + RESET + DIM + " (used " + usageCount + "x)" + RESET);
    }
    
    public static void printFlaminSuccess(String message) {
        System.out.println(GREEN + "  🔥✅ " + BOLD + message + RESET);
    }
    
    public static void printFlaminError(String message) {
        System.out.println(RED + "  🔥❌ " + BOLD + message + RESET);
    }
    
    public static void printFlaminWarning(String message) {
        System.out.println(YELLOW + "  🔥⚠️  " + BOLD + message + RESET);
    }
}
