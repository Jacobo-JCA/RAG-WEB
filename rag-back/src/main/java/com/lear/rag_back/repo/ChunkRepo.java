package com.lear.rag_back.repo;

import com.lear.rag_back.entity.ChunkDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChunkRepo extends JpaRepository<ChunkDocument, Long> {
    void deleteByDocumentIdDocument(Long idDocument);
    // <=> es el operador de distancia coseno entre dos vectores
    // CAST le pasamos el embedding como String desde Java en formato [0.1, -0.2, 0.3...]
    // y PostgreSQL necesita convertirlo al tipo vector para poder usar <=>.
    @Query(value = """
    SELECT c.* FROM chunks_documents c
    ORDER BY c.embedding <=> CAST(:embedding AS vector)
    LIMIT :topK
    """, nativeQuery = true)
    List<ChunkDocument> findMostSimilar(
            @Param("embedding") String embedding,
            @Param("topK") int topK
    );
}
