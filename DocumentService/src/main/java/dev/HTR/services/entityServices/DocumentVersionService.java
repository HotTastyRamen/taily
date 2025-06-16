package dev.HTR.services.entityServices;

import dev.HTR.DTOs.EntityesDTO.DocumentVersionDto;
import dev.HTR.entity.DocumentVersion;
import dev.HTR.repositories.DocumentVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentVersionService {

    private final DocumentVersionRepository repository;

    
    public DocumentVersionDto getById(Long id) {
        return toDto(repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DocumentVersion not found with ID: " + id)));
    }

    
    public Page<DocumentVersionDto> getAllByDocumentId(Long documentId, Pageable pageable) {
        return repository.findByDocumentId(documentId, pageable)
                .map(this::toDto);
    }

    
    public List<DocumentVersionDto> getAllByDocumentId(Long documentId) {
        return repository.findByDocumentId(documentId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    
    public Optional<DocumentVersionDto> getVersionByDocumentIdAndNumber(Long documentId, int versionNumber) {
        return repository.findByDocumentIdAndVersionNumber(documentId, versionNumber)
                .map(this::toDto);
    }

    
    public DocumentVersionDto getLatestVersion(Long documentId) {
        return toDto(repository.findTopByDocumentIdOrderByVersionNumberDesc(documentId));
    }

    
    public void deleteByDocumentId(Long documentId) {
        repository.deleteByDocumentId(documentId);
    }

    
    public long countByDocumentId(Long documentId) {
        return repository.countByDocumentId(documentId);
    }

    
    public Page<DocumentVersionDto> getAllByDateRange(LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return repository.findAllByUploadedAtBetween(from, to, pageable)
                .map(this::toDto);
    }

    
    public DocumentVersionDto save(DocumentVersion documentVersion) {
        return toDto(repository.save(documentVersion));
    }

    // Метод преобразования Entity в DTO
    private DocumentVersionDto toDto(DocumentVersion entity) {
        return new DocumentVersionDto(
                entity.getId(),
                entity.getVersionNumber(),
                entity.getUploadedAt(),
                entity.getFilePath(),
                entity.getDocument().getId()
        );
    }
}
