package com.lear.rag_back.controller;

import com.lear.rag_back.entity.Document;
import com.lear.rag_back.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("El file está vacío");
            }
            if (!file.getContentType().equals("application/pdf")) {
                return ResponseEntity.badRequest().body("Solo se aceptan PDFs");
            }
            Document doc = documentService.processPDF(file);
            return ResponseEntity.ok(doc);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error procesando el PDF: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Document>> getDocuments() {
        return ResponseEntity.ok(documentService.getDocuments());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
