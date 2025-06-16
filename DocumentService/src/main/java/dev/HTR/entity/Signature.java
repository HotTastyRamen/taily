package dev.HTR.entity;

import dev.HTR.entity.enumes.SignatureStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "signatures", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"document_id", "user_id"})
})
@Getter
@Setter
@NoArgsConstructor
public class Signature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private SignatureStatus status;

    private Instant signedAt;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;
}
