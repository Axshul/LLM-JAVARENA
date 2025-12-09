# 🚀 Setup Guide for LLM-JAVARENA

## Prerequisites

### 1. Install Java (JDK 11 or higher)

**Check if Java is installed:**
```bash
java -version
```

**If not installed, download from:**
- Oracle JDK: https://www.oracle.com/java/technologies/downloads/
- OpenJDK: https://adoptium.net/

### 2. Install Maven

**Check if Maven is installed:**
```bash
mvn -version
```

**If not installed:**

**Windows:**
1. Download Maven from: https://maven.apache.org/download.cgi
2. Extract to `C:\Program Files\Apache\maven`
3. Add to PATH:
   - Open System Properties → Environment Variables
   - Add `C:\Program Files\Apache\maven\bin` to PATH
   - Restart terminal

**Or use Chocolatey:**
```bash
choco install maven
```

**macOS:**
```bash
brew install maven
```

**Linux:**
```bash
sudo apt install maven  # Ubuntu/Debian
sudo yum install maven  # CentOS/RHEL
```

## 🔧 Configuration

### 1. Update API Keys

Open `src/main/java/org/example/App.java` and update the Gemini API keys:

```java
private static final String[] GEMINI_KEYS = {
    "AIzaSyDFSJrZO5-GBbJLdq8mGnejJuUwIALPec0",  // Your Key 1
    "AIzaSyD5Ul5g0OA3CLtRO9Qcp1h6DaK2hJGKyOg",  // Your Key 2
    "AIzaSyCmhsrtLnsCedmwOSidWg9bPFw66KwjPls"   // Your Key 3
};
```

### 2. Configure n8n Webhook (Optional)

If you have an n8n webhook, update this line in `App.java`:

```java
private static final String N8N_WEBHOOK = "https://your-n8n-instance.com/webhook/your-id";
```

**Note:** The current value is a placeholder. Replace it with your actual n8n webhook URL.

## 🏃 Running the Application

### Option 1: Using the Batch Script (Windows)

Simply double-click `run.bat` or run:
```bash
run.bat
```

### Option 2: Manual Build and Run

```bash
# Build the project
mvn clean package

# Run the application
java -jar target/LLM-JAVARENA-1.0-SNAPSHOT.jar
```

### Option 3: Using Maven Exec Plugin

```bash
mvn clean compile exec:java -Dexec.mainClass="org.example.App"
```

## 🎮 First Steps

Once the application starts:

1. You'll see the beautiful ASCII art banner
2. Type `help` to see all available commands
3. Try your first query:
   ```
   ⚡ > Hello, who are you?
   ```
4. Try the council mode:
   ```
   ⚡ > /council What is artificial intelligence?
   ```

## 🐛 Troubleshooting

### "Maven not found"
- Ensure Maven is installed and in your PATH
- Restart your terminal after installation
- Run `mvn -version` to verify

### "Java not found"
- Ensure Java 11+ is installed
- Set JAVA_HOME environment variable
- Run `java -version` to verify

### "API Error 400"
- Check if your Gemini API keys are valid
- Ensure you have API quota remaining
- Verify internet connection

### "Connection timeout"
- Check your internet connection
- Verify firewall settings
- Try increasing timeout in code if needed

### "ANSI colors not showing"
- Windows: Use Windows Terminal or enable ANSI support
- Some older terminals don't support ANSI colors
- The app will still work, just without colors

## 📚 Getting Gemini API Keys

1. Go to: https://makersuite.google.com/app/apikey
2. Sign in with your Google account
3. Click "Create API Key"
4. Copy the key and paste it in `App.java`

## 🎨 Customization

### Change LLM Colors

Edit `CLIRenderer.java` or `LLMCouncil.java` to customize colors:
```java
case "gemini-1": return Ansi.Color.GREEN;  // Change to your preferred color
```

### Add More LLMs

1. Create a new client class extending `LLMClient`
2. Implement `sendMessage()` and `isAvailable()` methods
3. Add to council in `App.java`:
```java
council.addMember(new YourNewClient("Name", "api-key"));
```

## 🌟 Tips

- Use `/council` for diverse perspectives on complex questions
- Use individual LLMs (`/gemini1`, `/gemini2`, etc.) for specific tasks
- Check `/history` to review your conversation
- Use `/clear` for a fresh screen

---

Need help? Check the README.md or create an issue on GitHub!
