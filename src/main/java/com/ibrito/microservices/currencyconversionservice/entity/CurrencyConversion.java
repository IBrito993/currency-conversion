package com.ibrito.microservices.currencyconversionservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversion {

    Long id;
    String from;
    String to;
    BigDecimal conversionMultiple;
    String environment;
    BigDecimal quantity;
    BigDecimal totalCalculatedAmount;


}
