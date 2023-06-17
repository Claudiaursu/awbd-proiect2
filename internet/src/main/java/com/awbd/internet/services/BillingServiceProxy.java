package com.awbd.internet.services;


import com.awbd.internet.model.Billing;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "billing")
public interface BillingServiceProxy {
    @GetMapping(value="/billing", consumes = "application/json")
    Billing findBilling();
}

