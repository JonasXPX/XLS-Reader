package com.github.jonasxpx.customer.generic;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DefaultBill {
    Long getBill();
    LocalDateTime getDate();
    String getPlate();
    String getDealership();
    String getTollPlaza();
    String getCategory();
    String getTag();
    BigDecimal getValue();
    boolean isVp();
}
