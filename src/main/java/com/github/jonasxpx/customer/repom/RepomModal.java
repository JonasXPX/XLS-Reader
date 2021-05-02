package com.github.jonasxpx.customer.repom;

import com.github.jonasxpx.Dates;
import com.github.jonasxpx.anotations.ColumnIdentify;
import com.github.jonasxpx.anotations.SheetObject;
import com.github.jonasxpx.anotations.Transformer;
import com.github.jonasxpx.customer.generic.Credit;
import com.github.jonasxpx.customer.generic.MonthlyPayment;
import com.github.jonasxpx.customer.repom.transformers.LocalDateTimeTransformer;
import com.github.jonasxpx.customer.repom.transformers.TipoLancamentoTransformer;
import com.github.jonasxpx.facade.CustomTransformer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@SheetObject(
        startAtColumn = 0,
        startAtRow = 6,
        endAtColumn = 18
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RepomModal {

    private Set<Credit> credits;
    private Set<MonthlyPayment> monthlyPayments;

    @ColumnIdentify(cellName = "Data Processamento")
    private String dataProcessamento;

    @ColumnIdentify(cellName = "Data Passagem")
    @Transformer(LocalDateTimeTransformer.class)
    private LocalDateTime dataPassagem;

    @ColumnIdentify(cellName = "placa")
    private String placa;

    @ColumnIdentify(cellName = "Tipo")
    @Transformer(value = TipoLancamentoTransformer.class)
    private TipoLancamento tipo;

    @ColumnIdentify(cellName = "ID Concessionária")
    private String idConcessionaria;

    @ColumnIdentify(cellName = "ID Praça")
    private String idPlaca;

    @ColumnIdentify(cellName = "descrição")
    private String descricao;

    @ColumnIdentify(cellName = "Categoria Cobrada")
    private String categoria;

    @ColumnIdentify(cellName = "Categoria Cadastrada")
    private String categoriaCadastrada;

    @ColumnIdentify(cellName = "Modelo Veículo")
    private String modeloVeiculo;

    @ColumnIdentify(cellName = "Marca Veículo")
    private String marcaVeiculo;

    @ColumnIdentify(cellName = "Valor Passagem")
    private Double valorPassagem;

    @ColumnIdentify(cellName = "Valor Vale Pedagio")
    private Double valorValePedagio;

    @ColumnIdentify(cellName = "Valor")
    private Double valor;

    @ColumnIdentify(cellName = "concessionária")
    private String concesionaria;

    @ColumnIdentify(cellName = "filtro", cellLocation = "1;4")
    @Transformer(value = CustomTransformer.class)
    private Dates filtro;

}
