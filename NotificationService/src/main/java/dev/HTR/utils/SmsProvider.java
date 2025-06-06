package dev.HTR.utils;

public interface SmsProvider {
    boolean sendSms(String phone, String text);
}
