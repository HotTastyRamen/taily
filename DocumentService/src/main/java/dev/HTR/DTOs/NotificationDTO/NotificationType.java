package dev.HTR.DTOs.NotificationDTO;

public enum NotificationType {

    SIGNATURE_NEEDED("SIGNATURE_NEEDED"),
    ACCOMPLISHED("ACCOMPLISHED");

    private String code;

    private NotificationType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
