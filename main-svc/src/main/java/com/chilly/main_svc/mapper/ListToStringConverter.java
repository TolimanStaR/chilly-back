package com.chilly.main_svc.mapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

@Converter
public class ListToStringConverter implements AttributeConverter<List<String>, String> {
    private final String DELIMITER = "<*>";
    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        return String.join(DELIMITER, strings);
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        return Arrays.asList(s.split(DELIMITER));
    }
}
