package dev.HTR.entities.auth.enumes.Converters;

import dev.HTR.entities.auth.enumes.Privileges;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;


@Converter(autoApply = true)
public class PrivilegeConverter implements AttributeConverter<Privileges, String> {

    @Override
    public String convertToDatabaseColumn(Privileges privileges) {
        if (privileges == null) {
            return null;
        }
        return privileges.getCode();
    }

    @Override
    public Privileges convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(Privileges.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
