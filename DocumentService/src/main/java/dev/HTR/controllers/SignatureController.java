package dev.HTR.controllers;

import dev.HTR.DTOs.EntityesDTO.SignatureDto;
import dev.HTR.DTOs.SignatureRequest;
import dev.HTR.entity.Signature;
import dev.HTR.services.entityServices.DocumentService;
import dev.HTR.services.entityServices.SignatureService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
    @RequestMapping("/signatures")
public class SignatureController {

    private final SignatureService signatureService;
    private final DocumentService documentService;

    @GetMapping("/{id}")
    public ResponseEntity<SignatureDto> getSignatureById(@PathVariable("id") Long id) {
        return signatureService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/assign")
    public ResponseEntity<?> createSignatures(@RequestBody SignatureRequest request) {
        signatureService.sendToSign(request.getDocumentId(), request.getUserIds());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assign/document/{documentId}")
    public ResponseEntity<?> createSignatures(@PathVariable Long documentId, Authentication authentication) {
        List<Long> userId = new ArrayList<>();
        userId.add((Long) authentication.getPrincipal());
        signatureService.sendToSign(documentId, userId);
        return ResponseEntity.ok().build();
    }

/*    @PatchMapping("/confirmDocument/{documentId}")
    public ResponseEntity<?> confirmSignature(
            @PathVariable Long documentId,
            Authentication authentication)
    {
        Long userId = (Long) authentication.getPrincipal();
        System.out.println(userId + " " + documentId);
        Signature signature = signatureService
                .getByUserIdAndDocumentId(userId, documentId);
        signatureService.confirmSignature(signature.getId());
        return ResponseEntity.ok(documentService.checkAllSigned(documentId));
    }*/

    @PatchMapping("/confirm/{signatureId}")
    public ResponseEntity<?> confirmSignature(
            @PathVariable Long signatureId,
            Authentication authentication)
    {
        signatureService.requestSignatureConfirmation(signatureId);
        return ResponseEntity.ok(signatureId);
    }

    @GetMapping("/document/{documentId}")
    public Page<SignatureDto> getSignaturesByDocumentId(
            @PathVariable Long documentId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<SignatureDto> signatures = signatureService.getSignaturesByDocumentId(documentId, pageable);
        return signatures;
    }

    @GetMapping("/user")
    public Page<SignatureDto> getAllByUserId(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "signedAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Long userId = (Long) authentication.getPrincipal();
        return signatureService.getAllByUserId(userId, pageable);
    }


}
