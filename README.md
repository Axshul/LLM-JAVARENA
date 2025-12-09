# 🤖 AI COUNCIL ARENA v2.0

**A Multi-Provider AI Command Center** - Chat with 8+ AI models simultaneously!

## ✨ What Makes This Special

- 🌐 **Multi-Provider Support**: Groq, OpenRouter, and Gemini APIs
- 🎨 **GUI-like CLI**: Beautiful terminal interface that looks modern
- ⚡ **8 AI Models**: Access to diverse AI models in one place
- 🆓 **Free Tier Models**: Uses free/low-cost models to avoid quota issues
- 🤝 **Council Mode**: Ask all models at once and compare responses
- 📊 **Token Tracking**: Monitor API usage in real-time
- 🔄 **Smart Fallbacks**: Auto-switches if one model fails
- 💾 **Conversation History**: Never lose context

## 🚀 Quick Start

**Just run:**
```bash
start.bat
```

That's it! The app auto-builds and runs.

## 💬 Commands

```
/council <message>       Ask all 8 AI models simultaneously
/ask <model> <message>   Chat with specific model
/models                  Show all available models
/tokens                  Show token usage stats
/history                 Show conversation history
/clear                   Clear screen
help                     Show help menu
exit                     Quit
```

**Quick chat:** Just type your message (no command needed)

## 🎯 Example Usage

```
>> /council What is quantum computing?
```
*All 8 models respond with their perspectives!*

```
>> /ask groq-llama-3.3-70b Explain AI in simple terms
```
*Chat with a specific powerful model*

```
>> Hello!
```
*Quick chat with first available model*

## 🤖 Available Models

### Groq (Ultra-Fast Inference)
- `groq-llama-3.3-70b` - Most capable, 70B parameters
- `groq-llama-3.1-8b` - Fast and efficient, 8B parameters
- `groq-mixtral-8x7b` - Mixture of experts, 8x7B

### OpenRouter (Diverse Selection)
- `openrouter-llama-3.1-8b` - Meta's Llama 3.1
- `openrouter-mistral-7b` - Mistral AI's 7B model
- `openrouter-phi-3` - Microsoft's Phi-3

### Gemini (Google AI - Backup)
- `gemini-1` - Gemini 2.0 Flash
- `gemini-2` - Gemini 1.5 Flash 8B

## 🎨 Features Showcase

### Beautiful UI
```
+---------------------------------------------------------------------------+
|                    AI  COUNCIL  ARENA  v2.0                              |
+---------------------------------------------------------------------------+
|  Multi-Provider AI Command Center                                        |
|  Powered by: Groq, OpenRouter & More                                     |
+---------------------------------------------------------------------------+
```

### Council Mode
```
+---------------------------------------------------------------------------+
|  COUNCIL SESSION - Consulting 8 AI Models                                |
+---------------------------------------------------------------------------+

[~] Querying groq-llama-3.3-70b...
[~] Querying groq-llama-3.1-8b...
[~] Querying groq-mixtral-8x7b...
...

 GROQ-LLAMA-3.3-70B 
 Quantum computing uses quantum mechanics principles...
    Tokens: 15 in / 120 out

 OPENROUTER-LLAMA-3.1-8B 
 Think of quantum computers as super-powered machines...
    Tokens: 15 in / 95 out

+---------------------------------------------------------------------------+
|  8 of 8 models responded successfully                                    |
+---------------------------------------------------------------------------+
```

### Model Status
```
 GROQ  groq-llama-3.3-70b (Fast inference) ONLINE
 GROQ  groq-llama-3.1-8b (Fast inference) ONLINE
 OPENROUTER  openrouter-llama-3.1-8b (Free tier) ONLINE
 GEMINI  gemini-1 (Google AI) ONLINE
```

## 🔧 Technical Details

### Architecture
- **Pure Java** - No external dependencies except Gson
- **Concurrent Processing** - Parallel API calls for speed
- **Smart Error Handling** - Graceful degradation
- **Token Tracking** - Real-time usage monitoring
- **Modular Design** - Easy to add new providers

### API Providers
1. **Groq** - Ultra-fast inference, generous free tier
2. **OpenRouter** - Access to multiple models, free tier available
3. **Gemini** - Google's AI, backup option

### Why These Models?
- ✅ **Free/Low-cost** - Won't exhaust quotas
- ✅ **Fast** - Quick responses
- ✅ **Diverse** - Different perspectives
- ✅ **Reliable** - High availability

## 📊 Token Usage

The app tracks:
- Input tokens (your prompts)
- Output tokens (AI responses)
- Total usage across all models
- Per-model statistics

View with: `/tokens`

## 🎓 Perfect for Demonstrations

This project showcases:
- ✅ API Integration (3 different providers)
- ✅ Concurrent Programming (parallel requests)
- ✅ Error Handling (fallbacks and retries)
- ✅ User Interface Design (beautiful CLI)
- ✅ Software Architecture (modular, extensible)
- ✅ Real-world Application (practical AI tool)

## 🛠️ Requirements

- Java 11 or higher
- Internet connection
- That's it!

## 📝 Project Structure

```
src/main/java/org/example/
├── App.java                 # Main application
├── CLIRenderer.java         # Beautiful UI rendering
├── LLMClient.java           # Abstract base class
├── GroqClient.java          # Groq API integration
├── OpenRouterClient.java    # OpenRouter API integration
├── GeminiClient.java        # Gemini API integration
├── LLMCouncil.java          # Multi-model orchestration
└── ConversationManager.java # History management
```

## 🌟 Highlights

1. **8 AI Models** - More than most commercial tools!
2. **3 API Providers** - Diverse and reliable
3. **Beautiful UI** - Looks professional
4. **Smart Fallbacks** - Never fails completely
5. **Token Tracking** - Cost awareness
6. **Council Mode** - Unique feature!
7. **One Command** - Super easy to run

## 🎯 Use Cases

- **Learning**: Compare how different AIs explain concepts
- **Brainstorming**: Get diverse creative ideas
- **Decision Making**: Multiple perspectives on choices
- **Code Help**: Different approaches to problems
- **Research**: Comprehensive answers from multiple sources

## 🚀 Future Enhancements

- [ ] Add more providers (Anthropic, Cohere, etc.)
- [ ] Streaming responses
- [ ] Conversation export
- [ ] Custom model selection
- [ ] Voice input/output
- [ ] Web interface

---

**Built with ❤️ for AI enthusiasts**

*Demonstrating the power of multi-provider AI integration!*
