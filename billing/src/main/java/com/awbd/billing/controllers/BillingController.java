package com.awbd.billing.controllers;

import com.awbd.billing.config.PropertiesConfig;
import com.awbd.billing.model.Billing;
import com.awbd.billing.services.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
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
public class BillingController {
    @Autowired
    private PropertiesConfig configuration;
    @Autowired
    Environment environment;

    private static final Logger logger = LoggerFactory.getLogger(BillingController.class);

    @Autowired
    BillingService billingService;


    @Operation(summary = "get all records for billing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "list of billings",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Billing.class))})})
    @GetMapping(value = "/billing/list", produces = {"application/hal+json"})
    public CollectionModel<Billing> findAll() {
        List<Billing> billingList = billingService.findAll();
        for (final Billing billing : billingList) {
            Link selfLink = linkTo(methodOn(BillingController.class).getBilling(billing.getId())).withSelfRel();
            billing.add(selfLink);
            Link deleteLink = linkTo(methodOn(BillingController.class).deleteBilling(billing.getId())).withRel("deleteBilling");
            billing.add(deleteLink);
        }

        Link link = linkTo(methodOn(BillingController.class).findAll()).withSelfRel();
        CollectionModel<Billing> billings = CollectionModel.of(billingList, link);

        return billings;
    }

    @Operation(summary = "find billing by amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "billings found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Billing.class))})})
    @GetMapping("/billing/amount/{amount}")
    CollectionModel<Billing> findByAmount(@PathVariable Integer amount) {
        List<Billing> billingList = billingService.findByAmount(amount);
        for (final Billing billing : billingList) {
            Link selfLink = linkTo(methodOn(BillingController.class).getBilling(billing.getId())).withSelfRel();
            billing.add(selfLink);
            Link deleteLink = linkTo(methodOn(BillingController.class).deleteBilling(billing.getId())).withRel("deleteBilling");
            billing.add(deleteLink);
        }

        Link link = linkTo(methodOn(BillingController.class).findByAmount(amount)).withSelfRel();
        CollectionModel<Billing> billingsByAmount = CollectionModel.of(billingList, link);

        return billingsByAmount;
    }

    @Operation(summary = "find billing by debt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "billings found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Billing.class))}),
            @ApiResponse(responseCode = "404", description = "Billing not found",
                    content = @Content)})
    @GetMapping("/billing/debt/{debt}")
    CollectionModel<Billing> findByDebt(@PathVariable Boolean debt) {
        List<Billing> billingList = billingService.findByDebt(debt);
        for (final Billing billing : billingList) {
            Link selfLink = linkTo(methodOn(BillingController.class).getBilling(billing.getId())).withSelfRel();
            billing.add(selfLink);
            Link deleteLink = linkTo(methodOn(BillingController.class).deleteBilling(billing.getId())).withRel("deleteBilling");
            billing.add(deleteLink);
        }

        Link link = linkTo(methodOn(BillingController.class).findByDebt(debt)).withSelfRel();
        CollectionModel<Billing> billingsByDebt = CollectionModel.of(billingList, link);

        return billingsByDebt;
    }

    @Operation(summary = "create new billing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "billing created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Billing.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)})
    @PostMapping("/billing")
    public ResponseEntity<Billing> save(@Valid @RequestBody Billing billing) {
        Billing savedBilling = billingService.save(billing);
        URI locationURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{billingId}").buildAndExpand(savedBilling.getId())
                .toUri();

        return ResponseEntity.created(locationURI).body(savedBilling);
    }

    @Operation(summary = "delete billing by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "billing deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Billing.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Billing not found",
                    content = @Content)})
    @DeleteMapping("/billing/delete/{billingId}")
    public Billing deleteBilling(@PathVariable Long billingId) {
        Billing billing = billingService.delete(billingId);

        return billing;
    }

    @Operation(summary = "get billing by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "billing found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Billing.class))}),
            @ApiResponse(responseCode = "404", description = "Billing not found",
                    content = @Content)})
    @GetMapping("/billing/{billingId}")
    public Billing getBilling(@PathVariable Long billingId){
        logger.info("get billing ....");
        Billing billing = billingService.findById(billingId);

        return billing;
    }
}
