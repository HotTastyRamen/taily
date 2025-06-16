package dev.HTR.DTOs.EntityesDTO;

import lombok.Data;

import java.time.Instant;

@Data
public class SignatureDto {
    private Long id;
    private Long userId;
    private String status;
    private Instant signedAt;
    private Long documentId;
}
