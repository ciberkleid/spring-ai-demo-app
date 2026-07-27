# spring-ai-demo

REST endpoints demonstrating Spring AI features, in order.

## Setup

In one terminal window, start the application:
```bash
direnv allow      # or: source .envrc
./mvnw spring-boot:run
```

In another terminal window, start ollama:
```bash
ollama serve
```

In a third terminal window, load the model:
```bash
direnv allow      # or: source .envrc
ollama pull ${SPRING_AI_OLLAMA_CHAT_OPTIONS_MODEL}
```

Wait for the model pull to finish.
Endpoints are documented via Swagger/OpenAPI (springdoc-openapi); opening
http://localhost:8080 redirects there:
```bash
clear && echo -e "\n########## OPENAPI SPEC ##########\n"
http :8080/v3/api-docs   # or: open http://localhost:8080
```

In the same terminal window, you can run each of the demos below.

## Demos

### 0. Setup — the pom and config that enable everything else
```bash
# bat -n -r 20:54 -H 31 -H 35 -H 39 pom.xml
idea --line 27 pom.xml
# bat -n -r 1:23 -H 13:20 src/main/java/com/example/demo/config/ChatClientConfig.java
idea --line 15 src/main/java/com/example/demo/config/ChatClientConfig.java
```

### 1. System instructions — plain chat with a system prompt
```bash
# bat -n -r 31:38 -H 34 src/main/java/com/example/demo/controller/BasicsController.java
idea --line 34 src/main/java/com/example/demo/controller/BasicsController.java
```
```bash
clear && echo -e "\n########## SYSTEM INSTRUCTIONS ##########\n"
http :8080/api/basics/system-instructions
```

### 2. Structured output — typed response instead of plain text
```bash
# bat -n -r 40:46 -H 41 -H 45 src/main/java/com/example/demo/controller/BasicsController.java
idea --line 41 src/main/java/com/example/demo/controller/BasicsController.java
```
```bash
clear && echo -e "\n########## STRUCTURED OUTPUT ##########\n"
http :8080/api/basics/structured-output
```

### 3. Chat memory — same `conversationId` across calls, with and without a memory advisor
```bash
idea --line 30 src/main/java/com/example/demo/controller/ChatMemoryController.java
```
```bash
clear && echo -e "\n########## CHAT MEMORY - WITHOUT MEMORY ##########\n"
http :8080/api/memory/chat message=="My name is Christian"
http :8080/api/memory/chat message=="What is my name?"
```
```bash
idea --line 39 src/main/java/com/example/demo/controller/ChatMemoryController.java
```
```bash
clear && echo -e "\n########## CHAT MEMORY - WITH MEMORY ##########\n"
http :8080/api/memory/chat-with-memory conversationId==demo message=="My name is Christian"
http :8080/api/memory/chat-with-memory conversationId==demo message=="What is my name?"
```

### 4. Prompt stuffing — system prompt + structured output + a document manually pasted into the prompt
```bash
# bat -n -r 48:59 -H 55 src/main/java/com/example/demo/controller/BasicsController.java
idea --line 55 src/main/java/com/example/demo/controller/BasicsController.java
idea --line 1 src/main/resources/spring-io-2026-schedule.md
```
```bash
clear && echo -e "\n########## PROMPT STUFFING (trimmed document) ##########\n"
http :8080/api/basics/prompt-stuffing     # Use with ollama, trimmed doc for faster result
```
```bash
clear && echo -e "\n########## PROMPT STUFFING (full document) ##########\n"
http :8080/api/basics/prompt-stuffing trimmed==false     # Use with more powerful models
```
```bash
idea --line 78 src/main/java/com/example/demo/controller/BasicsController.java
```
```bash
clear && echo -e "\n########## PROMPT STUFFING (World Cup fun facts) ##########\n"
http :8080/api/basics/prompt-stuffing-world-cup
```

### 5. Tool calling — model calls a Java method (weather lookup) to answer
```bash
# bat -n -r 40:48 -H 43 -H 44 src/main/java/com/example/demo/controller/ToolCallingController.java
idea --line 43 src/main/java/com/example/demo/controller/ToolCallingController.java
```
```bash
clear && echo -e "\n########## TOOL CALLING ##########\n"
http :8080/api/tools/weather
```

### 6. Tool calling + memory — combine the two
```bash
# bat -n -r 28:38 -r 50:59 -H 35 -H 36 -H 55 src/main/java/com/example/demo/controller/ToolCallingController.java
idea --line 35 src/main/java/com/example/demo/controller/ToolCallingController.java
```
```bash
clear && echo -e "\n########## TOOL CALLING + MEMORY ##########\n"
http :8080/api/tools/weather-with-memory conversationId==demo
```

### 7. Guardrails: safe input — blocks responses to sensitive input
```bash
# bat -n -r 25:34 -H 28:31 src/main/java/com/example/demo/controller/GuardrailsController.java
idea --line 28 src/main/java/com/example/demo/controller/GuardrailsController.java
```
```bash
clear && echo -e "\n########## GUARDRAILS: SAFE INPUT ##########\n"
http :8080/api/guardrails/safe-input
```

### 8. Guardrails: structured output validation — validates/retries structured output
```bash
# bat -n -r 36:48 -H 38:41 src/main/java/com/example/demo/controller/GuardrailsController.java
idea --line 38 src/main/java/com/example/demo/controller/GuardrailsController.java
```
```bash
clear && echo -e "\n########## GUARDRAILS: STRUCTURED OUTPUT VALIDATION ##########\n"
http :8080/api/guardrails/structured-output-validation
```

### 9. RAG — retrieval-augmented generation over a vector store (compare to step 4's manual stuffing)
```bash
idea --line 41 src/main/java/com/example/demo/controller/RagController.java
idea --line 23 src/main/java/com/example/demo/config/VectorStoreConfig.java
```
```bash
clear && echo -e "\n########## RAG ##########\n"
http :8080/api/rag/hurricane
```

All endpoints accept query params to override the defaults (see each controller).
