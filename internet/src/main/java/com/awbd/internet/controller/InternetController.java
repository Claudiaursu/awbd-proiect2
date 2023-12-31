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


    @Operation(summary = "get all records for internet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "list of internets",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Internet.class))})})
    @GetMapping(value = "/internet/list", produces = {"application/hal+json"})
    public CollectionModel<Internet> findAll( ) {

        List<Internet> internetList = internetService.findAll();
        for (final Internet internet : internetList) {
            Link selfLink = linkTo(methodOn(InternetController.class).getInternet(internet.getId())).withSelfRel();
            internet.add(selfLink);
            Link deleteLink = linkTo(methodOn(InternetController.class).deleteInternet(internet.getId())).withRel("deleteInternet");
            internet.add(deleteLink);
        }
        Link link = linkTo(methodOn(InternetController.class).findAll()).withSelfRel();
        CollectionModel<Internet> result = CollectionModel.of(internetList, link);
        return result;
    }

    @Operation(summary = "find internet by provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "internet found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Internet.class))}),
            @ApiResponse(responseCode = "404", description = "Internet not found",
                    content = @Content)})
    @GetMapping("/internet/provider/{provider}")
    public CollectionModel<Internet> findByProvider(@PathVariable String provider){
        List<Internet> internetList = internetService.findByProvider(provider);

        for (final Internet internet : internetList) {
            Link selfLink = linkTo(methodOn(InternetController.class).getInternet(internet.getId())).withSelfRel();
            internet.add(selfLink);
            Link deleteLink = linkTo(methodOn(InternetController.class).deleteInternet(internet.getId())).withRel("deleteInternet");
            internet.add(deleteLink);
        }
        Link link = linkTo(methodOn(InternetController.class).findByProvider(provider)).withSelfRel();
        CollectionModel<Internet> result = CollectionModel.of(internetList, link);
        return result;
    }

    @Operation(summary = "create new internet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "internet created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Internet.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)})
    @PostMapping("/internet")
    public ResponseEntity<Internet> save(@Valid @RequestBody Internet internet){
        Internet savedInternet = internetService.save(internet);
        URI locationUri =ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{internetId}").buildAndExpand(savedInternet.getId())
                .toUri();

        return ResponseEntity.created(locationUri).body(savedInternet);
    }


    @Operation(summary = "delete internet by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "internet deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Internet.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Internet not found",
                    content = @Content)})
    @DeleteMapping("/internet/delete/{internetId}")
    public Internet deleteInternet(@PathVariable Long internetId) {

        Internet internet = internetService.delete(internetId);
        return internet;
    }

    @Operation(summary = "get internet by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "internet found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Internet.class))}),
            @ApiResponse(responseCode = "404", description = "Internet not found",
                    content = @Content)})
    @GetMapping("/internet/{internetId}")
    @CircuitBreaker(name="billingById", fallbackMethod = "getInternetFallback")
    public Internet getInternet(@PathVariable Long internetId) {
        logger.info("Internet by id request start");
        Internet internet = internetService.findById(internetId);
        Billing billing = billingServiceProxy.findBilling();
        logger.info(billing.isDebt() == true? "Debt" : "No debt");
        if(billing.isDebt() == true)
            internet.setPrice(internet.getPrice() + billing.getAmount());
        logger.info("Internet by id request end");
        return internet;

    }

    private Internet getInternetFallback(Long internetId, Throwable throwable) {

        Internet internet = internetService.findById(internetId);
        return internet;
    }

}
