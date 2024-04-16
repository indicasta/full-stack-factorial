package com.indicasta.fullstackfactorial.customer;

import com.indicasta.fullstackfactorial.config.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final JWTService jwtService;

    public CustomerController(CustomerService customerService, JWTService jwtService) {
        this.customerService = customerService;
        this.jwtService = jwtService;
    }


}
