package dev.HTR.services.entityServices;

import dev.HTR.DTOs.EntityesDTO.DocumentDto;
import dev.HTR.DTOs.NotificationEvent;
import dev.HTR.entity.Document;
import dev.HTR.entity.DocumentVersion;
import dev.HTR.entity.Signature;
import dev.HTR.entity.enumes.DocumentStatus;
import dev.HTR.entity.enumes.SignatureStatus;
import dev.HTR.repositories.DocumentRepository;
import dev.HTR.repositories.DocumentVersionRepository;
import dev.HTR.repositories.SignatureRepository;
import dev.HTR.services.utilServices.KafkaProducerService;
import dev.HTR.services.utilServices.S3service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository versionRepository;
    private final SignatureRepository signatureRepository;
    private final S3service s3Service;
    private final KafkaProducerService kafkaProducerService;


    public DocumentDto createDocument(String title, String description, MultipartFile file) {
        Document document = new Document();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        document.setTitle(title);
        document.setDescription(description);
        document.setCreatedAt(Instant.now());
        document.setCreatorId((Long) auth.getPrincipal());
        document.setStatus(DocumentStatus.DRAFT);

        Document savedDoc = documentRepository.save(document);

        uploadNewVersion(savedDoc.getId(), file);

        return this.toDto(savedDoc);
    }

    public void uploadNewVersion(Long id, MultipartFile file) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        int nextVersion = document.getVersions().size() + 1;

        String path = s3Service.uploadFile(file, document.getId(), nextVersion);

        DocumentVersion version = new DocumentVersion();
        version.setVersionNumber(nextVersion);
        version.setUploadedAt(Instant.now());
        version.setFilePath(path);
        version.setDocument(document);

        versionRepository.save(version);

        document.setStatus(DocumentStatus.UPDATED);
        documentRepository.save(document);

        Page<Signature> page = signatureRepository.findByDocumentId(document.getId(), Pageable.unpaged());
        List<Signature> signatures = page.getContent();

        for (Signature sig : signatures) {
            sig.setStatus(SignatureStatus.PENDING);
            sig.setSignedAt(null);

            // Отправить сообщение через Kafka по sig.getUserId()
            NotificationEvent event = new NotificationEvent(sig.getUserId(), "document_assigned", null);
            kafkaProducerService.sendSmsRequest(event);
        }
        signatureRepository.saveAll(signatures);
    }

    public boolean checkAllSigned(Long documentId) {
        boolean allSigned = signatureRepository.findByDocumentId(documentId, Pageable.unpaged())
                .getContent()
                .stream()
                .allMatch(s -> s.getStatus() == SignatureStatus.ACCOMPLISHED);

        if (allSigned) {
            Document document = documentRepository.findById(documentId)
                    .orElseThrow(() -> new RuntimeException("Document not found"));
            document.setStatus(DocumentStatus.SIGNED);
            documentRepository.save(document);
            return true;
        }
        return false;
    }

    public Document findDocument(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    public Page<DocumentDto> getDocumentsCreatedByUser(Long userId, Pageable pageable) {
        return documentRepository.findAllByCreatorId(userId, pageable)
                .map(this::toDto);
    }

    public Page<DocumentDto> getDocumentsByStatus(DocumentStatus status, Pageable pageable) {
        return documentRepository.findAllByStatus(status, pageable)
                .map(this::toDto);
    }

    public Page<DocumentDto> searchDocumentsByTitle(String title, Pageable pageable) {
        return documentRepository.findAllByTitleContainingIgnoreCase(title, pageable)
                .map(this::toDto);
    }

    public Page<DocumentDto> searchDocumentsByCreatorAndTitle(
            Long creatorId,
            String titlePart,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return documentRepository.findByCreatorIdAndTitleContainingIgnoreCase(
                creatorId,
                titlePart == null ? "" : titlePart,
                pageRequest
        ).map(this::toDto);
    }

    private DocumentDto toDto(Document document) {
        DocumentDto dto = new DocumentDto();
        dto.setId(document.getId());
        dto.setTitle(document.getTitle());
        dto.setStatus(document.getStatus().toString());
        dto.setCreatedAt(document.getCreatedAt());

        List<DocumentVersion> versions = document.getVersions();
        if (versions != null && !versions.isEmpty()) {
            versions.sort(Comparator.comparingInt(DocumentVersion::getVersionNumber).reversed());
            dto.setLastVersionPath(versions.get(0).getFilePath());
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getPrincipal());
        Long userId = (Long) auth.getPrincipal();

        boolean isSigned = document.getSignatures().stream()
                .anyMatch(signature -> signature.getUserId().equals(userId) &&
                        signature.getStatus() == SignatureStatus.ACCOMPLISHED);
        dto.setSignedByUser(isSigned);

        return dto;
    }
}



