package com.ibrito.microservices.currencyconversionservice.controller;

import com.ibrito.microservices.currencyconversionservice.entity.CurrencyConversion;
import com.ibrito.microservices.currencyconversionservice.proxy.CurrencyExchangeProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currency-conversion")
public class CurrencyConversionController {

    @Value("${server.port}")
    String port;

    private final CurrencyExchangeProxy currencyExchangeProxy;

    @GetMapping("/from/{from}/to/{to}/quantity/{quantity}")
    public ResponseEntity<CurrencyConversion> retrieveValue(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        var response = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables );
        response.getBody().setEnvironment(port);
        response.getBody().setQuantity(quantity);
        response.getBody().setTotalCalculatedAmount(quantity.multiply(response.getBody().getConversionMultiple()));

        return response;
    }

    @GetMapping("/feign/from/{from}/to/{to}/quantity/{quantity}")
    public ResponseEntity<CurrencyConversion> retrieveValueFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {

        var response = currencyExchangeProxy.retrieveValue(from, to);

        response.getBody().setQuantity(quantity);
        response.getBody().setTotalCalculatedAmount(quantity.multiply(response.getBody().getConversionMultiple()));

        return response;
    }

}
