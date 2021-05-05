package com.github.jonasxpx.transformers;

import com.github.jonasxpx.customer.repom.Repom;
import com.github.jonasxpx.customer.repom.transformers.ValorPassagemTransformer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValorPassagemTransformerTest {

    @Test
    void shouldConvertToPositive() {
        BigDecimal out = new ValorPassagemTransformer()
                .run(Double.valueOf("-19.99"));


        assertEquals(BigDecimal.valueOf(19.99), out);
    }

    @Test
    void shouldConvertToNegative() {
        BigDecimal out = new ValorPassagemTransformer()
                .run(Double.valueOf("19.99"));

        assertEquals(BigDecimal.valueOf(-19.99), out);
    }

}
