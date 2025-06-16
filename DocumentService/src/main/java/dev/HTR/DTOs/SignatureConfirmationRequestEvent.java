package dev.HTR.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignatureConfirmationRequestEvent {
    private Long signatureId;
    private Long userId;
    private String recipient; // номер телефона
}
