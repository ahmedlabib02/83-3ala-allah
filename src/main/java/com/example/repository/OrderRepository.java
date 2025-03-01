package com.example.repository;

import com.example.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class OrderRepository {

    public OrderRepository() {

    }

    public void addOrder(Order order) {

    }

    public ArrayList<Order> getOrders() {
        return null;
    }

    public Order getOrderById(UUID orderId) {
        return null;
    }

    public void deleteOrderById(UUID orderId) {

    }

}
