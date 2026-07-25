package com.lear.rag_back.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocument;

    @Column(nullable = false)
    private String name;

    @Column(name = "date_uploaded")
    private LocalDateTime dateUploaded;

    @Column(name = "total_chunks")
    private Integer totalChunks;

    @PrePersist
    public void prePersist() {
        this.dateUploaded = LocalDateTime.now();
    }

    public Long getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(Long idDocument) {
        this.idDocument = idDocument;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(LocalDateTime dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public Integer getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(Integer totalChunks) {
        this.totalChunks = totalChunks;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(idDocument, document.idDocument);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idDocument);
    }

    @Override
    public String toString() {
        return "Document{" +
                "idDocument=" + idDocument +
                ", name='" + name + '\'' +
                ", dateUploaded=" + dateUploaded +
                ", totalChunks=" + totalChunks +
                '}';
    }
}