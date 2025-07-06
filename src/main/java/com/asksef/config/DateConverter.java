package com.asksef.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Converter(autoApply = true)
public class DateConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
        return (localDateTime != null) ?
                Timestamp.valueOf(localDateTime) :
                Timestamp.valueOf(LocalDateTime.now());
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
        return (timestamp != null) ? timestamp.toLocalDateTime() : LocalDateTime.now();
    }
}
