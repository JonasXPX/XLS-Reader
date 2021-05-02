package com.github.jonasxpx.customer.repom.transformers;

import com.github.jonasxpx.anotations.TransformerRunner;
import com.github.jonasxpx.customer.repom.TipoLancamento;

public class TipoLancamentoTransformer implements TransformerRunner<TipoLancamento> {
    @Override
    public TipoLancamento run(Object entity) {
        String tipo = (String) entity;
        tipo = tipo.toUpperCase().replaceAll("[ ]", "_");
        return TipoLancamento.valueOf(tipo);
    }
}
