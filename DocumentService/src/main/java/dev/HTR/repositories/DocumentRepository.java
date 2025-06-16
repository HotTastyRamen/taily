package dev.HTR.repositories;

import dev.HTR.entity.Document;
import dev.HTR.entity.enumes.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    // Пагинация по создателю
    Page<Document> findAllByCreatorId(Long creatorId, Pageable pageable);

    // Пагинация по статусу документа
    Page<Document> findAllByStatus(DocumentStatus status, Pageable pageable);

    @Query("SELECT d FROM Document d LEFT JOIN FETCH d.signatures WHERE d = :document")
    Optional<Document> findByEntityWithSignatures(
            @Param("document") Document document);

    // Поиск по части названия — уже корректно
    Page<Document> findAllByTitleContainingIgnoreCase(
            String title,
            Pageable pageable);

    Page<Document> findByCreatorIdAndTitleContainingIgnoreCase(
            Long creatorId,
            String titlePart,
            Pageable pageable);
    // Получение документа с подгрузкой подписей и версий
    @EntityGraph(attributePaths = {"signatures", "versions"})
    Optional<Document> findById(Long id);
}

