package dev.HTR.controllers;

import dev.HTR.DTOs.SignatureCodeConfirmationRequest;
import dev.HTR.DTOs.SignatureConfirmedEvent;
import dev.HTR.DTOs.VerJWT;
import dev.HTR.DTOs.VerificationCode;
import dev.HTR.services.CacheService;
import dev.HTR.services.KafkaProducerService;
import dev.HTR.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


@Controller
@AllArgsConstructor
public class VerificationController {

    @Autowired
    private final CacheService cacheService;

    @Autowired
    private final JwtUtils jwtUtils;

    @Autowired
    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/getVerToken")
    public ResponseEntity<?> getVerToken (@RequestBody VerificationCode verificationCode){

        if (verificationCode.getCode().equals(cacheService.getFromVerCache(verificationCode.getUserId()))){
            return ResponseEntity.ok(new VerJWT(
                    jwtUtils.generateTokenWithUserId(verificationCode.getUserId())));
        };

        return new ResponseEntity<String>(
                "Unauthorized. Issue with resolving given code",
                HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/confirmSignatureCode")
    public ResponseEntity<?> confirmSignatureCode(@RequestBody SignatureCodeConfirmationRequest request) {
        String expectedCode = String.valueOf(cacheService.getFromConfCache(request.getSignatureId()));

        if (expectedCode == null || !expectedCode.equals(request.getCode())) {
            Map<String, Object> response = Map.of(
                    "success", false,
                    "message", "Неверный код подтверждения"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Отправляем ивент в Kafka
        SignatureConfirmedEvent event = new SignatureConfirmedEvent(request.getSignatureId());
        kafkaProducerService.send("signature-confirmed", event);

        Map<String, Object> response = Map.of(
                "success", true,
                "message", "Подпись подтверждена",
                "signatureId", request.getSignatureId()
        );

        return ResponseEntity.ok(response);
    }



}
