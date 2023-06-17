package com.awbd.orders.controller;

import com.awbd.orders.model.Discount;
import com.awbd.orders.model.Order;
import com.awbd.orders.services.DiscountServiceProxy;
import com.awbd.orders.services.OrdersService;
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
public class OrdersController {
    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);


    @Autowired
    DiscountServiceProxy discountServiceProxy;

    @Autowired
    OrdersService ordersService;


    @GetMapping(value = "/orders/list", produces = {"application/hal+json"})
    public CollectionModel<Order> findAll( ) {

        List<Order> orders = ordersService.findAll();
        for (final Order order : orders) {
            Link selfLink = linkTo(methodOn(OrdersController.class).getSubscription(order.getId())).withSelfRel();
            order.add(selfLink);
            Link deleteLink = linkTo(methodOn(OrdersController.class).deleteSubscription(order.getId())).withRel("deleteSubscription");
            order.add(deleteLink);
        }
        Link link = linkTo(methodOn(OrdersController.class).findAll()).withSelfRel();
        CollectionModel<Order> result = CollectionModel.of(orders, link);
        return result;
    }


    @GetMapping("/orders/coach/{coach}/sport/{sport}")
    Order findByCoachAndSport(
            @PathVariable String coach,
            @PathVariable String sport){
        Order order = ordersService.findByCoachAndSport(coach, sport);


        Link selfLink = linkTo(methodOn(OrdersController.class).getSubscription(order.getId())).withSelfRel();

        order.add(selfLink);

        Discount discount = discountServiceProxy.findDiscount();
        log.info(discount.getVersionId());
        order.setPrice(order.getPrice()* (100 - discount.getMonth())/100);




        return order;
    }


    @PostMapping("/orders")
    public ResponseEntity<Order> save(@Valid @RequestBody Order order){
        Order savedOrder = ordersService.save(order);
        URI locationUri =ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{subscriptionId}").buildAndExpand(savedOrder.getId())
                .toUri();

        return ResponseEntity.created(locationUri).body(savedOrder);
    }


    @Operation(summary = "delete subscription by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "subscription deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Order.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Subscription not found",
                    content = @Content)})
    @DeleteMapping("/orders/{subscriptionId}")
    public Order deleteSubscription(@PathVariable Long subscriptionId) {

        Order order = ordersService.delete(subscriptionId);
        return order;
    }

    @GetMapping("/orders/{subscriptionId}")
    @CircuitBreaker(name="discountById", fallbackMethod = "getSubscriptionFallback")
    public Order getSubscription(@PathVariable Long subscriptionId) {
        logger.info("subscription by id request start");
        Order order = ordersService.findById(subscriptionId);
        Discount discount = discountServiceProxy.findDiscount();
        logger.info(discount.getVersionId());
        order.setPrice(order.getPrice()* (100 - discount.getMonth())/100);
        logger.info("subscription by id request end");
        return order;

    }

    private Order getSubscriptionFallback(Long subscriptionId, Throwable throwable) {

        Order order = ordersService.findById(subscriptionId);
        return order;
    }

}
