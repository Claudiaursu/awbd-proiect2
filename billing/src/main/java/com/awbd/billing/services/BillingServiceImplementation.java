package com.awbd.billing.services;

import com.awbd.billing.exceptions.BillingNotFound;
import com.awbd.billing.model.Billing;
import com.awbd.billing.repositories.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class BillingServiceImplementation implements BillingService {

    @Autowired
    BillingRepository billingRepository;

    @Override
    public List<Billing> findByAmount(Integer amount) {
        List<Billing> billingListByAmount = billingRepository.findByAmount(amount);

        return billingListByAmount;
    }

    @Override
    public List<Billing> findByDebt(Boolean debt) {
        List<Billing> billingListByDebt = billingRepository.findByDebt(debt);

        return billingListByDebt;
    }

    @Override
    public Billing save(Billing billingToSave) {
        Billing billing = billingRepository.save(billingToSave);

        return billing;
    }

    @Override
    public List<Billing> findAll() {
        List<Billing> billings = new LinkedList<>();
        billingRepository.findAll().iterator().forEachRemaining(billings::add);

        return billings;
    }

    @Override
    public Billing delete(Long id) {
        Optional<Billing> billingOptional = billingRepository.findById(id);
        if(!billingOptional.isPresent()) {
            throw new BillingNotFound("Billing with id " + id + " not found");
        }
        billingRepository.delete(billingOptional.get());

        return billingOptional.get();
    }

    @Override
    public Billing findById(Long id) {
        Optional<Billing> billingOptional = billingRepository.findById(id);
        if(!billingOptional.isPresent()) {
            throw new BillingNotFound("Billing with id " + id + " not found");
        }

        return billingOptional.get();
    }
}
