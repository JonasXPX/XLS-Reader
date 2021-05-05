package com.github.jonasxpx.customer.generic;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyPayment implements DefaultBill {

    private Long bill;
    private LocalDateTime date;
    private String plate;
    private String dealership;
    private String tollPlaza;
    private String category;
    private String tag;
    private BigDecimal value;
    private boolean vp;
    private LocalDateTime referenceDate;
    

}
