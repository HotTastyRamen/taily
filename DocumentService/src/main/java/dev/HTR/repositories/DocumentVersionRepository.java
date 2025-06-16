package dev.HTR.repositories;

import dev.HTR.entity.DocumentVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DocumentVersionRepository
        extends JpaRepository<DocumentVersion, Long> {

    // Получить все версии документа по его ID
    Page<DocumentVersion> findAllByDocumentId(Long documentId, Pageable pageable);

    // Найти все версии документа по ID документа с пагинацией
    Page<DocumentVersion> findByDocumentId(Long documentId, Pageable pageable);

    // Найти все версии документа по ID документа без пагинации
    List<DocumentVersion> findByDocumentId(Long documentId);

    // Найти последнюю (максимальную) версию документа по ID документа
    DocumentVersion findTopByDocumentIdOrderByVersionNumberDesc(Long documentId);

    // Удалить все версии по ID документа
    void deleteByDocumentId(Long documentId);

    // Подсчитать количество версий по ID документа
    long countByDocumentId(Long documentId);

    // Получить версии, созданные в заданный период
    Page<DocumentVersion> findAllByUploadedAtBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);

    // Получить конкретную версию по номеру и ID документа
    Optional<DocumentVersion> findByDocumentIdAndVersionNumber(Long documentId, int versionNumber);
}

