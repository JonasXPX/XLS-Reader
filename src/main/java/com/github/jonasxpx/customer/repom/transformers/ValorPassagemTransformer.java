package com.github.jonasxpx.customer.repom.transformers;

import com.github.jonasxpx.anotations.TransformerRunner;

import java.math.BigDecimal;

public class ValorPassagemTransformer implements TransformerRunner<BigDecimal> {
    @Override
    public BigDecimal run(Object entity) {
        Double valor = (Double) entity;
        return BigDecimal.valueOf(valor).negate();
    }
}
