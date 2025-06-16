package dev.HTR.repositories;

import dev.HTR.entity.DocumentVersion;
import dev.HTR.entity.Signature;
import dev.HTR.entity.enumes.SignatureStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SignatureRepository
        extends JpaRepository<Signature, Long> {

    // Получить все подписи по ID документа
    Page<Signature> findByDocumentId(Long documentId, Pageable pageable);

    Page<Signature> findAllByUserId(Long userId, Pageable pageable);

    Signature findByUserIdAndDocumentId(Long userId, Long documentId);

    // Получить подписи конкретного пользователя по ID
    Page<Signature> findByUserId(Long userId, Pageable pageable);

    // Найти подпись конкретного пользователя к документу
    Optional<Signature> findByDocumentIdAndUserId(Long documentId, Long userId);

    // Подсчитать количество подписей по статусу (для документа)
    long countByDocumentIdAndStatus(Long documentId, SignatureStatus status);

    // Подсчитать общее количество подписей по документу
    long countByDocumentId(Long documentId);

    // Найти все подписи с определённым статусом (например, PENDING)
    List<Signature> findByStatus(SignatureStatus status);

    // Найти все подписи пользователя с определённым статусом (например, просроченные подписи)
    Page<Signature> findByUserIdAndStatus(Long userId, SignatureStatus status, Pageable pageable);

    // Найти все подписи, подписанные за промежуток времени
    Page<Signature> findBySignedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);


}
