package com.github.jonasxpx;

import com.github.jonasxpx.customer.repom.Repom;
import com.github.jonasxpx.customer.repom.RepomModal;
import com.github.jonasxpx.facade.SheetBuilder;
import com.github.jonasxpx.facade.WorkBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class LerFaturaRepom {

    @Test
    void shouldReadRepom() {
        WorkBuilder.create("extrato.xls");
        List<RepomModal> repomModals = SheetBuilder.getInstance().read(RepomModal.class);

        Repom repom = new Repom(repomModals);

        assertFalse(repom.getCredits().isEmpty());

        System.out.println(repom.toString());
    }
}
