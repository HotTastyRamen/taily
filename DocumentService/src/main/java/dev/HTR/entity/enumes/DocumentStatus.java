package dev.HTR.entity.enumes;


import lombok.Data;
import lombok.Getter;

@Getter
public enum DocumentStatus {

    APPROVED("APPROVED"),
    SIGNING("SIGNING"),
    SIGNED("SIGNED"),
    UPDATED("UPDATED"),
    SENT("SENT"),
    DRAFT("DRAFT"),
    REJECTED("REJECTED");

    private final String code;

    DocumentStatus(String code) {
        this.code = code;
    }
}
