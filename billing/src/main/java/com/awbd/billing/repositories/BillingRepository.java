package com.awbd.billing.repositories;

import com.awbd.billing.model.Billing;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BillingRepository extends CrudRepository<Billing, Long> {

    List<Billing> findByAmount(Integer amount);
    List<Billing> findByDebt(Boolean debt);
}
