package com.lear.rag_back.service;

import com.lear.rag_back.entity.ChunkDocument;
import com.lear.rag_back.repo.ChunkRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final ChunkRepo chunkRepo;
    private final OllamaService ollamaService;

    @Value("${rag.top-k-results}")
    private int topK;

    public ChatService(ChunkRepo chunkRepo, OllamaService ollamaService) {
        this.chunkRepo = chunkRepo;
        this.ollamaService = ollamaService;
    }

    private String floatArrayToVectorString(float[] array) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public String answerQuestion(String ask) {
        float[] embeddingPregunta = ollamaService.generateEmbedding(ask);

        String embeddingStr = floatArrayToVectorString(embeddingPregunta);

        List<ChunkDocument> chunksRelevantes = chunkRepo.findMostSimilar(embeddingStr, topK);

        if (chunksRelevantes.isEmpty()) {
            return "No encontré documentos relevantes. Sube primero algunos PDFs.";
        }

        String context = chunksRelevantes.stream()
                .map(chunk -> String.format("[Fragmento %d del documento '%s']:\n%s",
                        chunk.getNumberChunk(),
                        chunk.getDocument().getName(),
                        chunk.getContent()))
                .collect(Collectors.joining("\n\n---\n\n"));

        return ollamaService.generateResponse(ask, context);
    }
}
