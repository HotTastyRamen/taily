package dev.HTR.DTOs;


import lombok.Data;

import java.util.List;

@Data
public class SignatureRequest {
    private List<Long> userIds;
    private Long documentId;
}

