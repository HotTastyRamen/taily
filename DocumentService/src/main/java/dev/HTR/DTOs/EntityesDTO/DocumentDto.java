package dev.HTR.DTOs.EntityesDTO;

import lombok.Data;

import java.time.Instant;

@Data
public class DocumentDto {
    private Long id;
    private String title;
    private String status;
    private Instant createdAt;
    private String lastVersionPath; // ссылка на файл
    private boolean isSignedByUser; // для assigned
}

