package com.lear.rag_back.service;

import com.lear.rag_back.entity.ChunkDocument;
import com.lear.rag_back.entity.Document;
import com.lear.rag_back.repo.ChunkRepo;
import com.lear.rag_back.repo.DocumentRepo;
import jakarta.transaction.Transactional;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepo documentRepo;
    private final ChunkRepo chunkRepo;
    private final ChunkingService chunkingService;
    private final OllamaService ollamaService;

    public DocumentService(DocumentRepo documentRepo,
                            ChunkRepo chunkRepo,
                            ChunkingService chunkingService,
                            OllamaService ollamaService) {
        this.documentRepo = documentRepo;
        this.chunkRepo = chunkRepo;
        this.chunkingService = chunkingService;
        this.ollamaService = ollamaService;
    }

    public List<Document> getDocuments() {
        return documentRepo.findAll();
    }

    @Transactional
    public void deleteDocument(Long id) {
        chunkRepo.deleteByDocumentIdDocument(id);
        documentRepo.deleteById(id);
    }
    // 1. Recibir el file
    private String extractTextPDF(MultipartFile file) throws IOException {
        // 2. Extraer los bytes del file y asignarlo a una libreria que entienda PDFs
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            // 3. Crear un object que obtenga el text del pdf
            PDFTextStripper stripper = new PDFTextStripper();
            // 4. Extract all el content textual
            // 5. Return el text en string
            return stripper.getText(document);
        }
    }

    @Transactional
    public Document processPDF(MultipartFile file) throws IOException {
        //1. Obtener el text completo de la extraccion
        String textComplete = extractTextPDF(file);
        //2. Usar un method para dividir en chunks
        List<String> chunks = chunkingService.divideEnChunks(textComplete);
        //3. Crear un Document antes de asociarlo a los chunks
        Document document = new Document();
        document.setName(file.getOriginalFilename());
        document.setTotalChunks(chunks.size());
        //4. Persistir el Document
        document = documentRepo.save(document);

        for (int i = 0; i < chunks.size(); i++) {
            String contentChunk = chunks.get(i);
            float[] embedding = ollamaService.generateEmbedding(contentChunk);

            ChunkDocument chunk = new ChunkDocument();
            chunk.setContent(contentChunk);
            chunk.setNumberChunk(i + 1);
            chunk.setEmbedding(embedding);
            chunk.setDocument(document);
            chunkRepo.save(chunk);
        }
        return document;
    }
}
