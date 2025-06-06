package dev.HTR.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class PrimarySmsProvider implements SmsProvider {

    private final RestTemplate restTemplate;

    @Value("${sms.primary.api-url}")
    private String apiUrl;

    @Value("${sms.primary.api-key}")
    private String apiKey;

    public PrimarySmsProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean sendSms(String phone, String text) {
        String url = String.format(
                "%s/sms/send?api_id=%s&to=%s&msg=%s&json=1",
                apiUrl,
                apiKey,
                phone,
                URLEncoder.encode(text, StandardCharsets.UTF_8)
        );

        try {
            // Получаем ответ как Map (или можешь создать DTO-класс)
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                Object status = response.getBody().get("status");
                System.out.println(response);
                System.out.println(status);
                return "OK".equals(status);
            } else {
                System.err.println("Ошибка от SMS API: " + response.getStatusCode());
                return false;
            }

        } catch (Exception e) {
            System.err.println("Primary SMS failed: " + e.getMessage());
            return false;
        }
    }
}

