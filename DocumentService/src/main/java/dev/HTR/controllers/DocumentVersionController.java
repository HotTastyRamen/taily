package dev.HTR.controllers;

import dev.HTR.DTOs.EntityesDTO.DocumentVersionDto;
import dev.HTR.entity.DocumentVersion;
import dev.HTR.services.entityServices.DocumentVersionService;
import dev.HTR.services.utilServices.S3service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/document-versions")
@RequiredArgsConstructor
public class DocumentVersionController {
    private final DocumentVersionService documentVersionService;
    private final S3service s3service;

    // Получить все версии документа по ID
    @GetMapping("/document/{documentId}")
    public Page<DocumentVersionDto> getAllVersionsByDocumentId(
            @PathVariable Long documentId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return documentVersionService.getAllByDocumentId(documentId, pageable);
    }

    // Получить конкретную версию документа
    @GetMapping("/document/{documentId}/version/{versionNumber}")
    public ResponseEntity<DocumentVersionDto> getVersionByNumber(
            @PathVariable Long documentId,
            @PathVariable int versionNumber
    ) {
        return documentVersionService
                .getVersionByDocumentIdAndNumber(documentId, versionNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Получить последнюю версию документа
    @GetMapping("/document/{documentId}/latest")
    public DocumentVersionDto getLatestVersion(@PathVariable Long documentId) {
        return documentVersionService.getLatestVersion(documentId);
    }

    // Получить верcии, загруженные в заданный период
    @GetMapping("/by-date-range")
    public Page<DocumentVersionDto> getByDateRange(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to,
            Pageable pageable
    ) {
        return documentVersionService.getAllByDateRange(from, to, pageable);
    }

    // Получить одну версию по её ID
    @GetMapping("/{id}")
    public DocumentVersionDto getById(@PathVariable Long id) {
        return documentVersionService.getById(id);
    }

    @GetMapping("/getVersion")
    public String getDoc(@RequestParam String objectKey) {
        System.out.println("getVersion");
        return s3service.generatePresignedDownloadUrl(objectKey);
    }

    // Удалить все версии по documentId
    @DeleteMapping("/document/{documentId}")
    public ResponseEntity<Void> deleteByDocumentId(@PathVariable Long documentId) {
        documentVersionService.deleteByDocumentId(documentId);
        return ResponseEntity.noContent().build();
    }

    // Создать новую версию (из готового объекта)
    @PostMapping
    public ResponseEntity<DocumentVersionDto> create(@RequestBody DocumentVersion documentVersion) {
        DocumentVersionDto saved = documentVersionService.save(documentVersion);
        return ResponseEntity.ok(saved);
    }
}
