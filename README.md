# spring-ai-demo

REST endpoints demonstrating Spring AI features, in order.

## Setup

```bash
brew install ollama
ollama serve &
ollama pull mistral
direnv allow      # or: source .envrc
./mvnw spring-boot:run
```

`GET /` lists all demos below.

## Demos

### 0. Setup — the pom and config that enable everything else

```bash
bat -n -r 20:54 -H 31 -H 35 -H 39 pom.xml
bat -n -r 1:23 -H 13:20 src/main/java/com/example/demo/config/ChatClientConfig.java
```

### 1. System instructions — plain chat with a system prompt
```bash
curl localhost:8080/api/basics/system-instructions
```
```bash
bat -n -r 31:38 -H 34 src/main/java/com/example/demo/controller/BasicsController.java
```

### 2. Structured output — typed response instead of plain text
```bash
curl localhost:8080/api/basics/structured-output
```
```bash
bat -n -r 40:46 -H 41 -H 45 src/main/java/com/example/demo/controller/BasicsController.java
```

### 3. Chat memory — stateful conversation (same `conversationId` across calls)
```bash
curl "localhost:8080/api/memory/chat?conversationId=demo&message=My%20name%20is%20Christian"
curl "localhost:8080/api/memory/chat?conversationId=demo&message=What%20is%20my%20name%3F"
```
```bash
bat -n -r 16:31 -H 19 -H 21 -H 28 src/main/java/com/example/demo/controller/ChatMemoryController.java
```

### 4. Prompt stuffing — system prompt + structured output + a document manually pasted into the prompt
```bash
curl localhost:8080/api/basics/prompt-stuffing
```
```bash
bat -n -r 48:59 -H 55 src/main/java/com/example/demo/controller/BasicsController.java
```

### 5. Tool calling — model calls a Java method (weather lookup) to answer
```bash
curl localhost:8080/api/tools/weather
```
```bash
bat -n -r 40:48 -H 43 -H 44 src/main/java/com/example/demo/controller/ToolCallingController.java
```

### 6. Tool calling + memory — combine the two
```bash
curl "localhost:8080/api/tools/weather-with-memory?conversationId=demo"
```
```bash
bat -n -r 28:38 -r 50:59 -H 35 -H 36 -H 55 src/main/java/com/example/demo/controller/ToolCallingController.java
```

### 7. Guardrails: safe input — blocks responses to sensitive input
```bash
curl localhost:8080/api/guardrails/safe-input
```
```bash
bat -n -r 25:34 -H 28:31 src/main/java/com/example/demo/controller/GuardrailsController.java
```

### 8. Guardrails: structured output validation — validates/retries structured output
```bash
curl localhost:8080/api/guardrails/structured-output-validation
```
```bash
bat -n -r 36:48 -H 38:41 src/main/java/com/example/demo/controller/GuardrailsController.java
```

### 9. RAG — retrieval-augmented generation over a vector store (compare to step 4's manual stuffing)
```bash
curl localhost:8080/api/rag/hurricane
```
```bash
bat -n -r 15:26 -H 19 src/main/java/com/example/demo/controller/RagController.java
bat -n -r 20:28 -H 27 src/main/java/com/example/demo/config/VectorStoreConfig.java
```

All endpoints accept query params to override the defaults (see each controller).
