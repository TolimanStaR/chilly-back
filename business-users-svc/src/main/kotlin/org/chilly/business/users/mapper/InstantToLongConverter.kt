package org.chilly.business.users.mapper

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.stereotype.Component
import java.time.Instant

@Component
@Converter
class InstantToLongConverter : AttributeConverter<Instant, Long> {

    override fun convertToDatabaseColumn(attribute: Instant?): Long {
       return attribute?.toEpochMilli() ?: 0L
    }

    override fun convertToEntityAttribute(dbData: Long?): Instant {
        return Instant.ofEpochMilli(dbData ?: 0L)
    }
}