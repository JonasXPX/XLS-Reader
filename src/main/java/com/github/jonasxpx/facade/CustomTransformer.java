package com.github.jonasxpx.facade;

import com.github.jonasxpx.Dates;
import com.github.jonasxpx.anotations.TransformerRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//example class
public class CustomTransformer implements TransformerRunner<Dates> {

    @Override
    public Dates run(Object entity) {
        String filter = (String) entity;
        String dates = filter.replaceAll("[A-Za-z: ]", "");
        int barIndex = dates.indexOf('|');
        LocalDate start = LocalDate.parse(dates.substring(0, barIndex), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate end = LocalDate.parse(dates.substring(barIndex +1), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return new Dates(start, end);
    }
}
