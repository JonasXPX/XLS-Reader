package com.github.jonasxpx;

import com.github.jonasxpx.customer.repom.RepomModal;
import com.github.jonasxpx.customer.repom.TipoLancamento;
import com.github.jonasxpx.facade.SheetBuilder;
import com.github.jonasxpx.facade.WorkBuilder;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static com.github.jonasxpx.customer.repom.TipoLancamento.PASSAGEM;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void shouldAnswerWithTrue() {
        WorkBuilder.create("extrato.xls");

        Set<Sheet> sheets = WorkBuilder.getInstance()
                .getSheet();

        assertEquals(1, sheets.size());
        Sheet sheet = sheets.iterator().next();

        assertNotNull(sheet);

        SheetBuilder instance = SheetBuilder.getInstance();

        List<RepomModal> read = instance.read(RepomModal.class);

        assertNotEquals(0, read.size());

        RepomModal modal = read.iterator().next();

        read.forEach(System.out::println);

        assertNotNull(modal);
        assertNotNull(modal.getCategoria());
        assertNotNull(modal.getConcesionaria());
        assertNotNull(modal.getCategoriaCadastrada());
        assertNotNull(modal.getDataProcessamento());
        assertNotNull(modal.getDataPassagem());
        assertNotNull(modal.getDescricao());
        assertNotNull(modal.getIdConcessionaria());
        assertNotNull(modal.getIdPlaca());

        System.out.printf("Object size: %s%n", read.size());
    }

}
