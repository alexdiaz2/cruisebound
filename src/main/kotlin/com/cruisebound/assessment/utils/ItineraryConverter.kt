package com.cruisebound.assessment.utils

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.util.stream.Collectors

@Converter
class ItineraryConverter : AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(p0: List<String>?): String {
        return if (p0.isNullOrEmpty())
            ""
        else
            p0.stream().collect(Collectors.joining(","))
    }

    override fun convertToEntityAttribute(p0: String?): List<String> {
        return p0?.split(",") ?: arrayListOf()
    }
}