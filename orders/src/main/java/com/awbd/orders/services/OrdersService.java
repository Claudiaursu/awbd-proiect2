package com.awbd.orders.services;

import com.awbd.orders.model.Order;

import java.util.List;

public interface OrdersService {
    Order findByCoachAndSport(String coach, String sport);
    Order save(Order order);
    List<Order> findAll();
    Order delete(Long id);
    Order findById(Long id);
}
