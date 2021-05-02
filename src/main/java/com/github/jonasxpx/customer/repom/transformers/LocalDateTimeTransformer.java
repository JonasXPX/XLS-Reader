package com.github.jonasxpx.customer.repom.transformers;

import com.github.jonasxpx.anotations.TransformerRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTransformer implements TransformerRunner<LocalDateTime> {
    @Override
    public LocalDateTime run(Object entity) {
        return LocalDateTime.parse((String) entity, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
