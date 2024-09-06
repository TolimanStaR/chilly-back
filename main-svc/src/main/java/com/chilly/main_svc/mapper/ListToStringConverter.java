package com.chilly.main_svc.mapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

@Converter
public class ListToStringConverter implements AttributeConverter<List<String>, String> {
    private final String DELIMITER = "###";
    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return null;
        }
        return String.join(DELIMITER, strings);
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        if (s == null) {
            return List.of();
        }
        return Arrays.asList(s.split(DELIMITER));
    }
}
