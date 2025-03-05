package com.evo.common.webapp.config.jackson;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomLocalDateSerializer
        extends JsonSerializer<LocalDate> { // Cấu hình serializer cho LocalDate với định dạng yyyy-MM-dd.
    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        if (localDate != null) {
            jsonGenerator.writeString(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }
}
