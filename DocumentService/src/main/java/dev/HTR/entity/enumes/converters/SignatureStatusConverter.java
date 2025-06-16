package dev.HTR.entity.enumes.converters;

import dev.HTR.entity.enumes.SignatureStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class SignatureStatusConverter implements
        AttributeConverter<SignatureStatus, java.lang.String> {

    @Override
    public String convertToDatabaseColumn (SignatureStatus status) {
        if (status == null) {
            return null;
        }
        return status.getCode();
    }

    @Override
    public SignatureStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(SignatureStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
