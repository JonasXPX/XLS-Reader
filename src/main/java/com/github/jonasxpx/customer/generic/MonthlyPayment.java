package com.github.jonasxpx.customer.generic;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyPayment {

    private LocalDateTime emission;
    private Long bill;
    private String plate;
    private LocalDateTime referenceDate;
    private BigDecimal value;
}
