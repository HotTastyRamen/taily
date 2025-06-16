package dev.HTR.services.utilServices;

import dev.HTR.DTOs.SignatureConfirmedEvent;
import dev.HTR.services.entityServices.SignatureService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaConsumerService {

    @Autowired
    private final SignatureService signatureService;

    @KafkaListener(
            topics = "signature-confirmed",
            groupId = "signature-confirmed-consumer",
            containerFactory = "signatureConfirmedKafkaListenerContainerFactory"
    )
    public void listen(SignatureConfirmedEvent signatureConfirmedEvent){
        signatureService.confirmSignature(signatureConfirmedEvent.getSignatureId());
        System.out.println(signatureService.getById(signatureConfirmedEvent.getSignatureId()));
    }
}
