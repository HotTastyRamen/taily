package dev.HTR.services;

import dev.HTR.utils.BackupSmsProvider;
import dev.HTR.utils.PrimarySmsProvider;
import dev.HTR.utils.SmsProvider;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private final SmsProvider primaryProvider;
    private final SmsProvider backupProvider;

    public SmsService(PrimarySmsProvider primary, BackupSmsProvider backup) {
        this.primaryProvider = primary;
        this.backupProvider = backup;
    }

    public boolean sendSms(String phone, String text) {
        boolean success = primaryProvider.sendSms(phone, text);
        if (!success) {
            System.out.println("Falling back to backup SMS provider...");
            return backupProvider.sendSms(phone, text);
        }
        return true;
    }
}

