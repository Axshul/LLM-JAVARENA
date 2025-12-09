# 📁 Project Structure

```
LLM-JAVARENA/
│
├── 📄 pom.xml                          # Maven configuration & dependencies
├── 📄 README.md                        # Main project documentation
├── 📄 SETUP.md                         # Detailed setup instructions
├── 📄 QUICKSTART.md                    # Quick start guide
├── 📄 FEATURES.md                      # Feature showcase
├── 📄 PROJECT_STRUCTURE.md             # This file
├── 📄 API_KEYS_TEMPLATE.txt            # API key configuration template
├── 📄 .gitignore                       # Git ignore rules
├── 🚀 run.bat                          # Windows run script
│
├── 📂 src/main/java/org/example/       # Source code
│   ├── 🎯 App.java                     # Main application entry point
│   ├── 🎨 CLIRenderer.java             # Terminal UI & ANSI rendering
│   ├── 🤖 LLMClient.java               # Abstract LLM client base class
│   ├── 🟢 GeminiClient.java            # Gemini API integration
│   ├── 🟡 N8NClient.java               # n8n webhook integration
│   ├── 👥 LLMCouncil.java              # Multi-LLM orchestration
│   └── 📜 ConversationManager.java     # Chat history management
│
├── 📂 target/                          # Compiled output (generated)
│   └── LLM-JAVARENA-1.0-SNAPSHOT.jar   # Executable JAR
│
└── 📂 .idea/                           # IDE configuration (optional)
```

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                         App.java                            │
│                    (Main Entry Point)                       │
│  • Initializes CLI                                          │
│  • Creates LLM Council                                      │
│  • Handles user input loop                                  │
│  • Command routing                                          │
└────────────┬────────────────────────────────────────────────┘
             │
             ├──────────────┬──────────────┬──────────────┐
             ▼              ▼              ▼              ▼
    ┌────────────┐  ┌──────────────┐  ┌──────────┐  ┌──────────┐
    │ CLIRenderer│  │  LLMCouncil  │  │Conversation│  │ Commands │
    │            │  │              │  │  Manager   │  │ Handler  │
    │ • Banner   │  │ • Orchestrate│  │            │  │          │
    │ • Colors   │  │ • Parallel   │  │ • History  │  │ • /council│
    │ • Prompts  │  │   calls      │  │ • Context  │  │ • /gemini1│
    │ • Messages │  │ • Responses  │  │ • Export   │  │ • /clear  │
    └────────────┘  └──────┬───────┘  └──────────┘  └──────────┘
                           │
                ┌──────────┼──────────┬──────────┐
                ▼          ▼          ▼          ▼
         ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐
         │ Gemini-1 │ │ Gemini-2 │ │ Gemini-3 │ │ n8n Agent│
         │  Client  │ │  Client  │ │  Client  │ │  Client  │
         └────┬─────┘ └────┬─────┘ └────┬─────┘ └────┬─────┘
              │            │            │            │
              ▼            ▼            ▼            ▼
         ┌────────────────────────────────────────────────┐
         │           External APIs                        │
         │  • Gemini API (Google)                         │
         │  • n8n Webhook                                 │
         └────────────────────────────────────────────────┘
```

## 📦 Core Components

### 1. **App.java** (Main Controller)
- Entry point of the application
- Initializes all components
- Manages the main CLI loop
- Routes commands to appropriate handlers
- Coordinates between Council and UI

### 2. **CLIRenderer.java** (UI Layer)
- ANSI color rendering
- ASCII art banner
- Formatted output (messages, errors, success)
- Visual indicators (thinking, prompts)
- Screen management (clear, separators)

### 3. **LLMClient.java** (Abstract Base)
- Defines interface for all LLM clients
- Common methods: `sendMessage()`, `isAvailable()`
- Ensures consistent behavior across providers

### 4. **GeminiClient.java** (Gemini Integration)
- Implements Gemini API calls
- JSON request/response handling
- Error handling and retries
- API key management

### 5. **N8NClient.java** (n8n Integration)
- Webhook POST requests
- JSON payload formatting
- Response parsing
- Workflow triggering

### 6. **LLMCouncil.java** (Orchestrator)
- Manages multiple LLM instances
- Concurrent API calls (parallel processing)
- Response aggregation
- Member management (add/remove LLMs)
- Single vs. council mode routing

### 7. **ConversationManager.java** (History)
- Stores conversation history
- User and LLM message tracking
- History display formatting
- Context management
- Export capabilities (future)

## 🔄 Data Flow

### Single LLM Query
```
User Input → App → Council → Specific LLM Client → API → Response
                                                           ↓
User ← CLIRenderer ← ConversationManager ← Council ← Response
```

### Council Mode Query
```
User Input → App → Council → ┌─ Gemini-1 → API ─┐
                              ├─ Gemini-2 → API ─┤→ Parallel
                              ├─ Gemini-3 → API ─┤   Execution
                              └─ n8n Agent → API ┘
                                      ↓
                              Aggregate Responses
                                      ↓
                              ConversationManager
                                      ↓
                                 CLIRenderer
                                      ↓
                                    User
```

## 🎨 Design Patterns Used

### 1. **Abstract Factory Pattern**
- `LLMClient` as abstract base
- Concrete implementations: `GeminiClient`, `N8NClient`

### 2. **Strategy Pattern**
- Different LLM strategies (Gemini, n8n)
- Interchangeable at runtime

### 3. **Facade Pattern**
- `CLIRenderer` simplifies complex ANSI operations
- `LLMCouncil` simplifies multi-LLM coordination

### 4. **Command Pattern**
- User commands (`/council`, `/gemini1`, etc.)
- Routed to appropriate handlers

### 5. **Singleton-like Pattern**
- Single `ConversationManager` instance
- Single `LLMCouncil` instance

## 📊 Dependencies

### External Libraries
```xml
<dependencies>
    <!-- JSON Processing -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
    
    <!-- ANSI Colors -->
    <dependency>
        <groupId>org.fusesource.jansi</groupId>
        <artifactId>jansi</artifactId>
        <version>2.4.0</version>
    </dependency>
</dependencies>
```

### Core Java Modules
- `java.net` - HTTP connections
- `java.io` - Input/Output streams
- `java.util.concurrent` - Parallel execution
- `java.nio` - Character encoding

## 🚀 Build Process

```
Source Code (.java)
       ↓
Maven Compile
       ↓
Compiled Classes (.class)
       ↓
Maven Package
       ↓
Executable JAR
       ↓
Run Application
```

## 🔧 Extension Points

### Adding New LLM Provider
1. Create new class extending `LLMClient`
2. Implement `sendMessage()` method
3. Implement `isAvailable()` method
4. Add to council in `App.java`

### Adding New Commands
1. Add command parsing in `App.java` main loop
2. Implement command handler
3. Update `CLIRenderer.printHelp()`

### Customizing UI
1. Modify `CLIRenderer.java`
2. Change colors, prompts, or formatting
3. Add new visual elements

---

**Clean, modular, and extensible architecture for the future of AI interaction!** 🏗️
