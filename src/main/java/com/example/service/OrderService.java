package com.example.service;

import com.example.model.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class OrderService extends MainService<Order> {

    public void addOrder(Order order) {

    }

    public ArrayList<Order> getOrders() {
        return null;
    }

    public Order getOrderById(UUID orderId) {
        return null;
    }

    public void deleteOrderById(UUID orderId) throws IllegalArgumentException {

    }

}
