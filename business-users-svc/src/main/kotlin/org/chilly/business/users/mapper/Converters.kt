package org.chilly.business.users.mapper

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.chilly.business.users.model.BusinessCategory

@Converter
class BusinessCategoryToStringConverter : AttributeConverter<List<BusinessCategory>, String> {

    override fun convertToEntityAttribute(dbData: String?): List<BusinessCategory> {
        return dbData?.split(Regex(LIST_SEPARATOR))
            ?.mapNotNull { str ->
                val fields = str.split(Regex(FIELD_SEPARATOR))
                runCatching {
                    BusinessCategory(fields[0], fields[1])
                }.getOrNull()
            } ?: emptyList()
    }

    override fun convertToDatabaseColumn(attribute: List<BusinessCategory>?): String {
        return attribute?.joinToString(separator = LIST_SEPARATOR) { bc -> "${bc.name}$FIELD_SEPARATOR${bc.code}" }
            ?: ""
    }

    companion object {
        private const val FIELD_SEPARATOR = "##"
        private const val LIST_SEPARATOR = "<->"
    }
}