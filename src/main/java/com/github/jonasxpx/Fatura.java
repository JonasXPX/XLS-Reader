package com.github.jonasxpx;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Fatura {

    private LocalDate emissao;

    private LocalDate vencimento;

    private BigDecimal valor;



}
