package com.lear.rag_back.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class OllamaService {
    @Value("${ollama.base-url}")
    private String baseUrl;

    @Value("${ollama.embedding-model}")
    private String embeddingModel;

    @Value("${ollama.chat-model}")
    private String chatModel;

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OllamaService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    private String post(String path, String jsonBody) throws IOException {
        RequestBody body = RequestBody.create(
                jsonBody, MediaType.get("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
                .url(baseUrl + path)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ollama error: " + response.code());
            }
            return response.body().string();
        }
    }

    public float[] generateEmbedding(String text) {
        try {
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", embeddingModel);
            requestBody.put("prompt", text);

            String responseJson = post("/api/embeddings", requestBody.toString());
            JsonNode response = objectMapper.readTree(responseJson);

            ArrayNode embeddingArray = (ArrayNode) response.get("embedding");
            float[] embedding = new float[embeddingArray.size()];
            for (int i = 0; i < embeddingArray.size(); i++) {
                embedding[i] = embeddingArray.get(i).floatValue();
            }

            return embedding;

        } catch (Exception e) {
            throw new RuntimeException("Error generando embedding: " + e.getMessage(), e);
        }
    }

    public String generateResponse(String question, String context) {
        try {
            String prompt = String.format("""
            Usa ÚNICAMENTE la siguiente información para responder la question.
            Si la información no es suficiente, dilo claramente.
            
            context:
            %s
            
            question: %s
            
            RESPUESTA:
            """, context, question);

            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", chatModel);
            requestBody.put("prompt", prompt);
            requestBody.put("stream", false);

            String responseJson = post("/api/generate", requestBody.toString());
            JsonNode response = objectMapper.readTree(responseJson);
            return response.get("response").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error generando respuesta: " + e.getMessage(), e);
        }
    }
}
