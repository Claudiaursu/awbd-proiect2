package com.awbd.billing.services;

import com.awbd.billing.model.Billing;

import java.util.List;

public interface BillingService {

    List<Billing> findByAmount(Integer amount);
    List<Billing> findByDebt(Boolean debt);
    Billing save(Billing billing);
    List<Billing> findAll();
    Billing delete(Long id);
    Billing findById(Long id);
}
