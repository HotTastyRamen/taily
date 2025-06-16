package dev.HTR.services;
import dev.HTR.DTOs.NotificationEvent;
import dev.HTR.DTOs.SignatureConfirmationRequestEvent;
import dev.HTR.DTOs.VerificationCode;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class KafkaConsumerService {

    private final SmsService smsService;

    private CacheService cacheService;

    @Autowired
    private RestTemplate restTemplate;

    // URL сервиса, где работает эндпоинт. Замените на ваш URL и порт
    private static final String USER_SERVICE_URL = "http://localhost:8080/users/{id}/phone";

    private String fetchPhoneNumberByUserId(Long userId) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(USER_SERVICE_URL, String.class, userId);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Пользователь не найден: userId = " + userId);
        } catch (Exception e) {
            System.out.println("Ошибка при обращении к Auth-сервису: " + e.getMessage());
        }
        return null;
    }

    @KafkaListener(topics = "test-topic", groupId = "NotificationGroup")
    public void listen(NotificationEvent message) {

        System.out.println("Получено сообщение: " + message);

        System.out.println(message.getRecipient() + message.getType() + message.getUserId());

        String recipient = message.getRecipient();
        if (recipient == null || recipient.isBlank()) {
            recipient = fetchPhoneNumberByUserId(message.getUserId());
            if (recipient == null) {
                System.out.println("Не удалось получить номер телефона. Прерываем обработку.");
                return;
            }
        }

        VerificationCode code;

        switch (message.getType()) {
            case "document_assigned":
                String assignText = "Привет, это Taily! На вас назначена новая подпись документа. Подробности - в личном кабинете ЭДО";
                System.out.println(recipient + assignText);
                //smsService.sendSms(recipient, assignText);
                break;
            case "document_sign":
                code = new VerificationCode(cacheService.createVerificationCode(message.getUserId()));
                System.out.println(recipient + "Код подписи документа:" +code.getCode().toString());
                //smsService.sendSms(recipient, "Код подписи документа:" +code.getCode().toString());
                break;
            case "verification":
                code = new VerificationCode(cacheService.createVerificationCode(message.getUserId()));
                System.out.println(recipient + "Код верификации:" + code.getCode().toString());
                //smsService.sendSms(recipient, "Код верификации:" + code.getCode().toString());
                break;
            default:
                System.out.println("Неизвестный тип уведомления: " + message.getType());
        }

        //smsService.sendSms(message.getRecipient(), code.getCode().toString());
    }

    @KafkaListener(topics = "signature-confirmation-request", groupId = "SignatureNotificationGroup")
    public void listenSignatureConfirmation(SignatureConfirmationRequestEvent event) {
        System.out.println("Получено событие подтверждения подписи: " + event);

        String recipient = event.getRecipient();
        if (recipient == null || recipient.isBlank()) {
            recipient = fetchPhoneNumberByUserId(event.getUserId());
            if (recipient == null) {
                System.out.println("Не удалось получить номер телефона для userId = " + event.getUserId());
                return;
            }
        }

        VerificationCode code;

        code = new VerificationCode(cacheService.createConfirmationCode(event.getSignatureId()));
        System.out.println("Отправляем код на номер: " + recipient + " | Код: " + code.getCode());

        //smsService.sendSms(recipient, "Ваш код для подтверждения подписи: " + code);
    }
}
