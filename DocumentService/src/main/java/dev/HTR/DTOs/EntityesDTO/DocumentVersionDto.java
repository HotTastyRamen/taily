package dev.HTR.DTOs.EntityesDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class DocumentVersionDto {

    private Long id;
    private int versionNumber;
    private Instant uploadedAt;
    private String filePath;
    private Long documentId;
}
