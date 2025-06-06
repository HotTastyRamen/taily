package dev.HTR.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class BackupSmsProvider implements SmsProvider {

    private final RestTemplate restTemplate;

    @Value("${sms.backup.email}")
    private String email;

    @Value("${sms.backup.apiKey}")
    private String apiKey;

    public BackupSmsProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean sendSms(String phone, String text) {
        try {
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);

            String url = String.format(
                    "https://%s:%s@gate.smsaero.ru/v2/sms/send?number=%s&text=%s&sign=%s",
                    email,
                    apiKey,
                    phone,
                    encodedText,
                    URLEncoder.encode("SMS Aero", StandardCharsets.UTF_8) // sign
            );

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> body = response.getBody();
                if (body == null || !Boolean.TRUE.equals(body.get("success"))) {
                    return false;
                }

                Map<String, Object> data = (Map<String, Object>) body.get("data");

                Integer status = (Integer) data.get("status");

                // 1 — доставлено, 3 — передано, 0 — в очереди (опционально: тоже можно считать успешными)
                return status != null && (status == 0 || status == 1 || status == 3);
            }

            return false;

        } catch (Exception e) {
            System.err.println("Backup SMS failed: " + e.getMessage());
            return false;
        }
    }

}

