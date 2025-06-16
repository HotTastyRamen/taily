package dev.HTR.DTOs;

import lombok.Data;

@Data
public class SignatureCodeConfirmationRequest {
    private Long signatureId;
    private String code;
}
