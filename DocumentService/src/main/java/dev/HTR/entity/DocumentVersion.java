package dev.HTR.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity

@Getter
@Setter
@NoArgsConstructor
@Table(name = "document_versions")
public class DocumentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int versionNumber;

    private Instant uploadedAt;

    private String filePath; // путь в бакете или прямая ссылка

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;
}
