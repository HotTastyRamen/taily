package dev.HTR.entities.auth.enumes;

public enum Privileges {
    WRITING_COMMENTS_PRIVILEGE("WriteComms"),
    ADMIN_PRIVILEGE("BeAdmin"),
    READ_PRIVILEGE("Read");

    private String code;

    private Privileges(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
