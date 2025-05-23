package dev.HTR.entities.auth.enumes.Converters;

import dev.HTR.entities.auth.enumes.Roles;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Roles, String> {

    @Override
    public String convertToDatabaseColumn(Roles roles) {
        if (roles == null) {
            return null;
        }
        return roles.getCode();
    }

    @Override
    public Roles convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(Roles.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
