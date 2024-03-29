package de.thb.fbi.msr.maus.todolist.remote;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//This is to make things work with LocalDateTime objects, even though they do not work with JSON
public class LocalDateTimeSerializer extends JsonSerializer <LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String formattedDateTime = value.format(FORMATTER);
        gen.writeString(formattedDateTime);
    }
}
