package dev.HTR.services.entityServices;

import dev.HTR.DTOs.NotificationEvent;
import dev.HTR.DTOs.EntityesDTO.SignatureDto;
import dev.HTR.DTOs.SignatureConfirmationRequestEvent;
import dev.HTR.entity.Document;
import dev.HTR.entity.Signature;
import dev.HTR.entity.enumes.DocumentStatus;
import dev.HTR.entity.enumes.SignatureStatus;
import dev.HTR.repositories.DocumentRepository;
import dev.HTR.repositories.SignatureRepository;
import dev.HTR.services.utilServices.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SignatureService {

    private final SignatureRepository signatureRepository;
    private final DocumentRepository documentRepository;
    private final KafkaProducerService kafkaProducerService;

    public void requestSignatureConfirmation(Long signatureId) {
        Signature signature = signatureRepository.findById(signatureId)
                .orElseThrow(() -> new RuntimeException("Signature not found"));

        SignatureConfirmationRequestEvent event = new SignatureConfirmationRequestEvent(
                signature.getId(),
                signature.getUserId(),
                null
        );

        kafkaProducerService.sendSignatureConfirmationRequest(event);
    }


    public void confirmSignature(Long signatureId) {
        Signature signature = signatureRepository.findById(signatureId)
                .orElseThrow(() -> new RuntimeException("Signature not found"));

        signature.setStatus(SignatureStatus.ACCOMPLISHED);
        signature.setSignedAt(Instant.now());

        signatureRepository.save(signature);

        Document document = documentRepository.findByEntityWithSignatures(signature.getDocument())
                .orElseThrow(() -> new RuntimeException("Document not found"));

        boolean allSigned = document.getSignatures().stream()
                .allMatch(s -> s.getStatus() == SignatureStatus.ACCOMPLISHED);

        if (allSigned) {
            document.setStatus(DocumentStatus.SIGNED);
            documentRepository.save(document);
        }
    }

    public List<SignatureDto> sendToSign(Long documentId, List<Long> userIds) {
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        doc.setStatus(DocumentStatus.SENT);
        documentRepository.save(doc);

        List<Signature> signatures = userIds.stream().map(userId -> {
            Signature s = new Signature();
            s.setUserId(userId);
            s.setDocument(doc);
            s.setStatus(SignatureStatus.PENDING);
            return s;
        }).toList();

        List<Signature> createdSigns = signatureRepository.saveAll(signatures);

        // Уведомление
        for (Long userId : userIds) {
            NotificationEvent event = new NotificationEvent(userId, "document_assigned", null);
            kafkaProducerService.sendSmsRequest(event);
        }

        return createdSigns
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Signature getByUserIdAndDocumentId(Long userId, Long documentId){
        return signatureRepository.findByUserIdAndDocumentId(userId, documentId);
    }

    public Page<SignatureDto> getSignaturesByDocumentId(Long documentId, Pageable pageable) {
        Page<SignatureDto> signatureDtoPage= signatureRepository.findByDocumentId(documentId, pageable)
                .map(this::toDto);
        System.out.println("getSignaturesByDocumentId" + signatureDtoPage);
        return signatureDtoPage;
    }

    public Page<SignatureDto> getAllByUserId(Long userId, Pageable pageable) {
        return signatureRepository.findAllByUserId(userId, pageable)
                .map(this::toDto);
    }

    public Optional<SignatureDto> getById(Long signatureId){
        return signatureRepository.findById(signatureId).map(this::toDto);
    }
    private SignatureDto toDto(Signature signature) {
        SignatureDto dto = new SignatureDto();
        dto.setId(signature.getId());
        dto.setUserId(signature.getUserId());
        dto.setStatus(signature.getStatus().toString());
        dto.setSignedAt(signature.getSignedAt());

        if (signature.getDocument() != null) {
            dto.setDocumentId(signature.getDocument().getId());
        }

        return dto;
    }
}
