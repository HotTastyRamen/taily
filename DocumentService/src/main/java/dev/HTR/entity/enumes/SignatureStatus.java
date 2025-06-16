package dev.HTR.entity.enumes;

import lombok.Getter;

@Getter
public enum SignatureStatus {
    PENDING("PENDING"),
    ACCOMPLISHED("ACCOMPLISHED");

    private final String code;

    SignatureStatus(String code) {
        this.code = code;
    }
}
