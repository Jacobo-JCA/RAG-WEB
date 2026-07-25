package com.lear.rag_back.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkingService {
    @Value("${rag.chunk-size}")
    private int chunkSize;

    @Value("${rag.chunk-overlap}")
    private int chunkOverlap;

    public List<String> divideEnChunks(String text) {
        List<String> chunks = new ArrayList<>();

        String textClean = text
                .replaceAll("\\s+", " ")
                .trim();

        if (textClean.isEmpty()) {
            return chunks;
        }

        int start = 0;
        int iteraciones = 0;
        while (start < textClean.length()) {
            iteraciones++;
            if (iteraciones > 10000) {
                throw new RuntimeException("Bucle infinito detectado. start=" + start + " length=" + textClean.length());
            }
            int end = Math.min(start + chunkSize, textClean.length());

            if (end < textClean.length()) {
                int lastSpace = textClean.lastIndexOf(' ', end);
                if (lastSpace > start) {
                    end = lastSpace;
                }
            }

            String chunk = textClean.substring(start, end).trim();
            if (!chunk.isEmpty()) {
                chunks.add(chunk);
            }

            if (end == textClean.length()) {
                break;
            }
            start = end - chunkOverlap;
            if (start >= end) start = end + 1;
        }

        return chunks;
    }
}
