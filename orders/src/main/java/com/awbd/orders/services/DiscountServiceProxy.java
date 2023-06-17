package com.awbd.orders.services;


import com.awbd.orders.model.Discount;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "discount")
public interface DiscountServiceProxy {
    @GetMapping(value="/discount", consumes = "application/json")
    Discount findDiscount();
}

