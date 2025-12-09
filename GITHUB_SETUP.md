# 🔒 GitHub Setup Guide

## ⚠️ **IMPORTANT: Protecting Your API Keys**

**NEVER commit API keys to GitHub!** This guide shows you how to keep them safe.

---

## 🚀 **Quick Setup**

### **1. Clone the Repository**
```bash
git clone https://github.com/yourusername/LLM-JAVARENA.git
cd LLM-JAVARENA
```

### **2. Create Your .env File**
```bash
# Copy the example file
copy .env.example .env

# Edit .env and add your actual API keys
notepad .env
```

### **3. Add Your API Keys**

Edit `.env` and replace the placeholder values:

```env
# Groq API Key (get from: https://console.groq.com)
GROQ_API_KEY=your_actual_groq_key_here

# OpenRouter API Key (get from: https://openrouter.ai/keys)
OPENROUTER_API_KEY=your_actual_openrouter_key_here

# Gemini API Keys (get from: https://makersuite.google.com/app/apikey)
GEMINI_KEY_1=your_actual_gemini_key_1_here
GEMINI_KEY_2=your_actual_gemini_key_2_here
GEMINI_KEY_3=your_actual_gemini_key_3_here

# n8n Webhook URL (optional)
N8N_WEBHOOK_URL=your_n8n_webhook_url_here
```

### **4. Run the App**
```bash
start.bat
```

---

## 🔑 **Getting API Keys**

### **Groq API** (Free, Fast)
1. Go to: https://console.groq.com
2. Sign up / Log in
3. Go to API Keys section
4. Create new key
5. Copy to `.env` file

### **OpenRouter API** (Free tier available)
1. Go to: https://openrouter.ai
2. Sign up / Log in
3. Go to: https://openrouter.ai/keys
4. Create new key
5. Copy to `.env` file

### **Gemini API** (Free tier available)
1. Go to: https://makersuite.google.com/app/apikey
2. Sign in with Google
3. Click "Create API Key"
4. Copy to `.env` file
5. Repeat for 3 keys (optional - can use same key)

### **n8n Webhook** (Optional)
- If you have your own n8n instance, add the webhook URL
- Otherwise, leave the default or remove this line

---

## 📁 **File Structure**

```
LLM-JAVARENA/
├── .env                    # YOUR API KEYS (NOT in git)
├── .env.example            # Template (safe to commit)
├── .gitignore              # Protects .env from git
├── src/
│   └── main/java/org/example/
│       ├── App.java        # Uses Config.get() - NO hardcoded keys
│       ├── Config.java     # Loads keys from .env
│       └── ...
└── ...
```

---

## ✅ **What's Safe to Commit**

✅ `.env.example` - Template with placeholders
✅ `.gitignore` - Protects sensitive files
✅ `App.java` - Uses Config.get() (no hardcoded keys)
✅ `Config.java` - Loads from .env
✅ All other source files
✅ Documentation

---

## ❌ **What's NOT Safe to Commit**

❌ `.env` - Contains your actual API keys
❌ Any file with hardcoded API keys
❌ `target/` directory (compiled files)
❌ IDE-specific files

---

## 🛡️ **Security Checklist**

Before pushing to GitHub:

- [ ] `.env` is in `.gitignore`
- [ ] No hardcoded API keys in source files
- [ ] `.env.example` has only placeholders
- [ ] Test that app works with `.env` file
- [ ] Run: `git status` - ensure `.env` is not tracked

---

## 🔍 **Check for Leaked Keys**

Before first commit:
```bash
# Make sure .env is ignored
git status

# Should NOT show .env in the list
# If it does, run:
git rm --cached .env
```

---

## 🚀 **For Your Teacher**

If your teacher needs to run the project:

1. **Share the repository** (without .env)
2. **Teacher creates their own .env** from .env.example
3. **Teacher adds their own API keys**
4. **Teacher runs start.bat**

**OR**

For demo purposes, you can:
1. Create a separate `.env.demo` with temporary/limited keys
2. Share that file separately (not in git)
3. Teacher renames it to `.env`

---

## 📝 **Alternative: Environment Variables**

Instead of `.env` file, you can use system environment variables:

**Windows:**
```cmd
set GROQ_API_KEY=your_key_here
set OPENROUTER_API_KEY=your_key_here
set GEMINI_KEY_1=your_key_here
java -cp "target\classes;target\lib\*" org.example.App
```

**Linux/Mac:**
```bash
export GROQ_API_KEY=your_key_here
export OPENROUTER_API_KEY=your_key_here
export GEMINI_KEY_1=your_key_here
java -cp "target/classes:target/lib/*" org.example.App
```

---

## 🎓 **For Submission**

When submitting to your teacher:

**Option 1: GitHub Repository**
- Push code to GitHub (without .env)
- Share repository link
- Include GITHUB_SETUP.md instructions
- Teacher adds their own keys

**Option 2: ZIP File**
- Include `.env.example`
- Include instructions to create `.env`
- DO NOT include your actual `.env`

**Option 3: Demo Keys**
- Create temporary API keys with limits
- Share separately (email, not in git)
- Revoke after demo

---

## 🔒 **Best Practices**

1. **Never commit .env** - Always in .gitignore
2. **Use .env.example** - Template for others
3. **Rotate keys regularly** - Especially after sharing
4. **Use limited keys** - For demos, create keys with usage limits
5. **Monitor usage** - Check API dashboards for unexpected usage

---

## 🆘 **If You Accidentally Committed Keys**

**IMMEDIATELY:**

1. **Revoke the keys** in the API provider dashboard
2. **Remove from git history:**
```bash
git filter-branch --force --index-filter \
  "git rm --cached --ignore-unmatch .env" \
  --prune-empty --tag-name-filter cat -- --all
```
3. **Force push:**
```bash
git push origin --force --all
```
4. **Create new keys**
5. **Add to new .env file**

---

**Stay safe! Your API keys are valuable!** 🔐
