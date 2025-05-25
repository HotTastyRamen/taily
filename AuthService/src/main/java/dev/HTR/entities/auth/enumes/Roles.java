package dev.HTR.entities.auth.enumes;

import lombok.Getter;

@Getter
public enum Roles {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String code;

    private Roles(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
