package com.awbd.orders.services;

import com.awbd.orders.exceptions.*;
import com.awbd.orders.model.Order;
import com.awbd.orders.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    OrderRepository orderRepository;

    @Override
    public Order findByCoachAndSport(String coach, String sport) {
        Order order = orderRepository.findByCoachAndSport(coach,sport);
        return order;
    }

    @Override
    public Order save(Order order) {
        Order orderSave = orderRepository.save(order);
        return orderSave;
    }

    @Override
    public List<Order> findAll(){
        List<Order> orders = new LinkedList<>();
        orderRepository.findAll().iterator().forEachRemaining(orders::add);
        return orders;
    }

    @Override
    public Order delete(Long id){
        Optional<Order> subscriptionOptional = orderRepository.findById(id);
        if (! subscriptionOptional.isPresent())
            throw new OrderNotFound("Subscription " + id + " not found!");
        orderRepository.delete(subscriptionOptional.get());
        return subscriptionOptional.get();
    }

    @Override
    public Order findById(Long id) {
        Optional<Order> subscriptionOptional = orderRepository.findById(id);
        if (! subscriptionOptional.isPresent())
            throw new OrderNotFound("Subscription " + id + " not found!");
        return subscriptionOptional.get();
    }


}
