package com.lear.rag_back.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Objects;

@Entity
@Table(name = "chunks_documents")
public class ChunkDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChunk;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "number_chunk")
    private Integer numberChunk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_document", nullable = false)
    private Document document;
    // 768 cantidad de dimensiones del vector, varia con el model
    @Column(
            name = "embedding",
            columnDefinition = "vector(768)"
    )
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 3)
    private float[] embedding;

    public Long getIdChunk() {
        return idChunk;
    }

    public void setIdChunk(Long idChunk) {
        this.idChunk = idChunk;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getNumberChunk() {
        return numberChunk;
    }

    public void setNumberChunk(Integer numberChunk) {
        this.numberChunk = numberChunk;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChunkDocument that = (ChunkDocument) o;
        return Objects.equals(idChunk, that.idChunk);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idChunk);
    }

    @Override
    public String toString() {
        return "CheckDocument{" +
                "idChunk=" + idChunk +
                ", content='" + content + '\'' +
                ", numberChunk=" + numberChunk +
                '}';
    }
}
