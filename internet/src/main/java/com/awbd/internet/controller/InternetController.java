package com.awbd.internet.controller;

import com.awbd.internet.model.Billing;
import com.awbd.internet.model.Internet;
import com.awbd.internet.services.BillingServiceProxy;
import com.awbd.internet.services.InternetService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@Slf4j
public class InternetController {
    private static final Logger logger = LoggerFactory.getLogger(InternetController.class);


    @Autowired
    BillingServiceProxy billingServiceProxy;

    @Autowired
    InternetService internetService;


    @GetMapping(value = "/internet/list", produces = {"application/hal+json"})
    public CollectionModel<Internet> findAll( ) {

        List<Internet> internetList = internetService.findAll();
        for (final Internet internet : internetList) {
            Link selfLink = linkTo(methodOn(InternetController.class).getSubscription(internet.getId())).withSelfRel();
            internet.add(selfLink);
            Link deleteLink = linkTo(methodOn(InternetController.class).deleteSubscription(internet.getId())).withRel("deleteSubscription");
            internet.add(deleteLink);
        }
        Link link = linkTo(methodOn(InternetController.class).findAll()).withSelfRel();
        CollectionModel<Internet> result = CollectionModel.of(internetList, link);
        return result;
    }


    @GetMapping("/internet/provider/{provider}")
    Internet findByProvider(
            @PathVariable String provider){
        Internet internet = internetService.findByProvider(provider);


        Link selfLink = linkTo(methodOn(InternetController.class).getSubscription(internet.getId())).withSelfRel();

        internet.add(selfLink);

        Billing billing = billingServiceProxy.findBilling();
        log.info(billing.isDebt() == true? "Debt" : "No debt");
        if(billing.isDebt() == true)
            internet.setPrice(internet.getPrice() + billing.getAmount());
        return internet;
    }


    @PostMapping("/internet")
    public ResponseEntity<Internet> save(@Valid @RequestBody Internet internet){
        Internet savedInternet = internetService.save(internet);
        URI locationUri =ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{subscriptionId}").buildAndExpand(savedInternet.getId())
                .toUri();

        return ResponseEntity.created(locationUri).body(savedInternet);
    }


    @Operation(summary = "delete subscription by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "subscription deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Internet.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Subscription not found",
                    content = @Content)})
    @DeleteMapping("/internet/{subscriptionId}")
    public Internet deleteSubscription(@PathVariable Long subscriptionId) {

        Internet internet = internetService.delete(subscriptionId);
        return internet;
    }

    @GetMapping("/internet/{subscriptionId}")
    @CircuitBreaker(name="billingById", fallbackMethod = "getSubscriptionFallback")
    public Internet getSubscription(@PathVariable Long subscriptionId) {
        logger.info("subscription by id request start");
        Internet internet = internetService.findById(subscriptionId);
        Billing billing = billingServiceProxy.findBilling();
        logger.info(billing.isDebt() == true? "Debt" : "No debt");
        if(billing.isDebt() == true)
            internet.setPrice(internet.getPrice() + billing.getAmount());
        logger.info("subscription by id request end");
        return internet;

    }

    private Internet getSubscriptionFallback(Long subscriptionId, Throwable throwable) {

        Internet internet = internetService.findById(subscriptionId);
        return internet;
    }

}
