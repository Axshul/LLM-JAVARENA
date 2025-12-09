# ⚡ Quick Start Guide

## 🏃 Get Running in 3 Steps

### Step 1: Install Prerequisites
```bash
# Check Java (need 11+)
java -version

# Check Maven
mvn -version
```

If missing, see [SETUP.md](SETUP.md) for installation instructions.

### Step 2: Configure API Keys

Edit `src/main/java/org/example/App.java`:
```java
private static final String[] GEMINI_KEYS = {
    "YOUR_KEY_1",  // Replace with your Gemini API key
    "YOUR_KEY_2",  // Replace with your Gemini API key
    "YOUR_KEY_3"   // Replace with your Gemini API key
};
```

Get keys at: https://makersuite.google.com/app/apikey

### Step 3: Run!

**Windows:**
```bash
run.bat
```

**Or manually:**
```bash
mvn clean package
java -jar target/LLM-JAVARENA-1.0-SNAPSHOT.jar
```

## 🎮 Essential Commands

| What You Want | Command | Example |
|---------------|---------|---------|
| Quick chat | Just type | `Hello!` |
| Ask all LLMs | `/council <msg>` | `/council Explain AI` |
| Ask Gemini #1 | `/gemini1 <msg>` | `/gemini1 Write a poem` |
| Ask Gemini #2 | `/gemini2 <msg>` | `/gemini2 Debug this code` |
| Ask Gemini #3 | `/gemini3 <msg>` | `/gemini3 Translate to Spanish` |
| Trigger n8n | `/n8n <msg>` | `/n8n Create task` |
| View history | `/history` | `/history` |
| Clear screen | `/clear` | `/clear` |
| Show help | `help` | `help` |
| Exit | `exit` | `exit` |

## 💡 Try These First

```bash
# 1. Simple greeting
Hello, who are you?

# 2. Get multiple perspectives
/council What is the meaning of life?

# 3. Creative task
/gemini1 Write a haiku about programming

# 4. Check your conversation
/history

# 5. Fresh start
/clear
```

## 🎯 Common Use Cases

### Brainstorming
```
/council Give me 5 startup ideas for AI in education
```

### Code Help
```
/gemini1 Explain this Java code: [paste code]
```

### Learning
```
/council Explain quantum computing like I'm 10
```

### Content Creation
```
/gemini2 Write a professional email about project delays
```

### Comparison
```
/council What are pros and cons of microservices?
```

## 🐛 Quick Troubleshooting

| Problem | Solution |
|---------|----------|
| Maven not found | Install Maven, add to PATH, restart terminal |
| Java not found | Install JDK 11+, set JAVA_HOME |
| API Error 400 | Check API keys are valid |
| No colors | Use Windows Terminal or modern terminal |
| Connection timeout | Check internet, firewall settings |

## 📚 Learn More

- **Full Setup**: [SETUP.md](SETUP.md)
- **All Features**: [FEATURES.md](FEATURES.md)
- **Documentation**: [README.md](README.md)

## 🎨 What Makes This Special?

✨ **Beautiful CLI** - Colorful, modern terminal interface
🤖 **AI Council** - Multiple LLMs working together
⚡ **Fast** - Concurrent API calls
📜 **History** - Never lose context
🎯 **Simple** - Intuitive commands
🔧 **Extensible** - Easy to add more LLMs

---

**Ready to experience the future of AI chat?** 🚀

Run `run.bat` and start chatting!
