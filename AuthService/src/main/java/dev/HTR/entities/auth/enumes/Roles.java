package dev.HTR.entities.auth.enumes;

import lombok.Getter;

@Getter
public enum Roles {
    USER("USER"),
    ADMIN("ADMIN");

    private String code;

    private Roles(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
