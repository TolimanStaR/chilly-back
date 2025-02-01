package com.chilly.places_svc.mapper

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.util.Collections.emptyList

@Converter
class ListToStringConverter : AttributeConverter<List<String>?, String?> {

    override fun convertToDatabaseColumn(strings: List<String>?): String? {
        if (strings.isNullOrEmpty()) {
            return null
        }
        return strings.joinToString(separator = DELIMITER)
    }

    override fun convertToEntityAttribute(s: String?): List<String> {
        s ?: return emptyList()
        return s.split(DELIMITER.toRegex())
    }

    companion object {
        const val DELIMITER = "###"
    }
}