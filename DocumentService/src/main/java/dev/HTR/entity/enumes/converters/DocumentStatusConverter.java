package dev.HTR.entity.enumes.converters;

import dev.HTR.entity.enumes.DocumentStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class DocumentStatusConverter implements
        AttributeConverter<DocumentStatus, String> {

    @Override
    public String convertToDatabaseColumn(DocumentStatus status) {
        if (status == null) {
            return null;
        }
        return status.getCode();
    }

    @Override
    public DocumentStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(DocumentStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
