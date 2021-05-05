package com.github.jonasxpx;

import com.github.jonasxpx.customer.repom.Repom;
import com.github.jonasxpx.customer.repom.RepomModal;
import com.github.jonasxpx.facade.SheetBuilder;
import com.github.jonasxpx.facade.WorkBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class LerFaturaRepom {

    @Test
    void shouldReadRepom() {
        File file = new File("faturas");
        for (File faturas : file.listFiles()) {

            WorkBuilder.create(faturas.getAbsolutePath());
            List<RepomModal> repomModals = SheetBuilder.getInstance().read(RepomModal.class);

            Repom repom = new Repom(repomModals);

            assertFalse(repom.getCredits().isEmpty());

            System.out.println(faturas.getName());
            System.out.println(repom.toString());
        }
    }
}
