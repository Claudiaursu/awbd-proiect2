package com.awbd.billing.controllers;

import com.awbd.billing.config.PropertiesConfig;
import com.awbd.billing.model.Billing;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Controller {
    @Autowired
    private PropertiesConfig configuration;
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    @Autowired
    Environment environment;


    @GetMapping("/billing")
    public Billing getBilling(){
        logger.info("get billing ....");
        return new Billing(configuration.getAmount(),configuration.isDebt());
    }
}
