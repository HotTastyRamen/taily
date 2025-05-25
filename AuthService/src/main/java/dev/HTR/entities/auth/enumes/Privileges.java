package dev.HTR.entities.auth.enumes;

public enum Privileges {
    WRITING_COMMENTS_PRIVILEGE("writeComms"),
    ADMIN_PRIVILEGE("beAdmin"),
    READ_PRIVILEGE("read");

    private String code;

    private Privileges(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
