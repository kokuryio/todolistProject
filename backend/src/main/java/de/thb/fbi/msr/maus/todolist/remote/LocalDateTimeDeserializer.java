package de.thb.fbi.msr.maus.todolist.remote;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//This is to make things work with LocalDateTime objects, even though they do not work with JSON
public class LocalDateTimeDeserializer extends JsonDeserializer <LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateTimeString = p.getValueAsString();
        return LocalDateTime.parse(dateTimeString, FORMATTER);
    }
}
