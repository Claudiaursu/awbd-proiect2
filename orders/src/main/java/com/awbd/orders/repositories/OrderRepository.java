package com.awbd.orders.repositories;

import com.awbd.orders.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Order findByCoachAndSport(String coach, String sport);
}
