package com.github.jonasxpx;

import com.github.jonasxpx.facade.SheetBuilder;
import com.github.jonasxpx.facade.WorkBuilder;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void shouldAnswerWithTrue() {
        WorkBuilder.create("extrato.xls");

        Set<Sheet> sheets = WorkBuilder.getInstance()
                .getSheet();

        assertEquals(1, sheets.size());
        Sheet sheet = sheets.iterator().next();

        assertNotNull(sheet);

        SheetBuilder instance = SheetBuilder.getInstance();

        MyModal read = instance.read(MyModal.class);
        System.out.println(read.toString());
    }
}
