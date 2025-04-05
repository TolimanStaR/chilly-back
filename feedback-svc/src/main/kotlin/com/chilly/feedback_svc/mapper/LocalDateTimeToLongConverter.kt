package com.chilly.feedback_svc.mapper

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Component
@Converter
class LocalDateTimeToLongConverter : AttributeConverter<LocalDateTime, Long> {

    override fun convertToDatabaseColumn(attribute: LocalDateTime?): Long {
        attribute ?: return -1L
        return attribute.toInstant(ZoneOffset.UTC).toEpochMilli()
    }

    override fun convertToEntityAttribute(dbData: Long?): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(dbData ?: 0), ZoneId.of("UTC"))
    }
}