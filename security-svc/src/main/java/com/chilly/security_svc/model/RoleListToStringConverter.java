package com.chilly.security_svc.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

@Converter
public class RoleListToStringConverter implements AttributeConverter<List<Role>, String> {

    private static final String LIST_SEPARATOR = "##";

    @Override
    public String convertToDatabaseColumn(List<Role> attribute) {
        if (attribute == null) return "";
        return String.join(LIST_SEPARATOR, attribute.stream().map(Role::name).toList());
    }

    @Override
    public List<Role> convertToEntityAttribute(String dbData) {
        if (dbData == null) return List.of();
        return Arrays.stream(dbData.split(LIST_SEPARATOR))
                .map(Role::valueOf)
                .toList();
    }
}
