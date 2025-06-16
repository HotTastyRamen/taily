package dev.HTR.controllers;

import dev.HTR.DTOs.EntityesDTO.DocumentDto;
import dev.HTR.entity.enumes.DocumentStatus;
import dev.HTR.services.entityServices.DocumentService;
import dev.HTR.services.utilServices.S3service;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final S3service service;
    private final DocumentService documentService;

    @PostMapping(path = "/uploadDoc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDoc(
            @RequestPart("file") MultipartFile file,
            @RequestPart("name") String name,
            @RequestPart("description") String description) {
        try {
            return ResponseEntity.ok(documentService.createDocument(name, description, file));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/{id}/upload-version",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadNewVersion(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file
    ) {
        documentService.uploadNewVersion(id, file);
        return ResponseEntity.ok("Новая версия успешно загружена");
    }

    @GetMapping("/created")
    public Page<DocumentDto> getCreatedDocuments(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = (Long) authentication.getPrincipal();
        return documentService.getDocumentsCreatedByUser(userId, pageable);
    }

    @GetMapping("/UserDocs")
    public Page<DocumentDto> searchDocuments(
            Authentication authentication,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Long userId = (Long) authentication.getPrincipal();
        return documentService.searchDocumentsByCreatorAndTitle(
                userId, title, page, size, sortBy, sortDir
        );
    }

    @GetMapping("/search")
    public Page<DocumentDto> searchDocumentsByTitle(
            @RequestParam String title,
            @PageableDefault(size = 10) Pageable pageable) {
        return documentService.searchDocumentsByTitle(title, pageable);
    }

    @GetMapping("/by-status")
    public Page<DocumentDto> getDocumentsByStatus(
            @RequestParam DocumentStatus status,
            @PageableDefault(size = 10) Pageable pageable) {
        return documentService.getDocumentsByStatus(status, pageable);
    }

    @GetMapping("/getDocDownloadLink")
    public String getDocDownloadLink(@RequestParam String objectKey) {
        return service.generatePresignedDownloadUrl(objectKey);
    }



}
