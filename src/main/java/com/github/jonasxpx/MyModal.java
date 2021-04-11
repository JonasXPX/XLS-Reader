package com.github.jonasxpx;

import com.github.jonasxpx.anotations.ColumnIdentify;
import com.github.jonasxpx.anotations.SheetObject;
import com.github.jonasxpx.anotations.Transformer;
import com.github.jonasxpx.facade.CustomTransformer;
import lombok.*;

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
public class MyModal {

    @ColumnIdentify(cellName = "Data Processamento")
    private String dataProcessamento;

    @ColumnIdentify(cellName = "Data Passagem")
    private String dataPassagem;

    @ColumnIdentify(cellName = "placa")
    private String placa;

    @ColumnIdentify(cellName = "Tipo")
    @Transformer(value = CustomTransformer.class)
    private String tipo;

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



}
