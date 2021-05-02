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
public class Credit {

    private LocalDateTime date;
    private String plate;
    private String description;
    private Long bill;
    private LocalDate emission;
    private BigDecimal value;
    private Long plaza;
    private boolean isValePedagio;
}
