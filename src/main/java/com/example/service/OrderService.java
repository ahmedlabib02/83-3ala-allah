package com.example.service;

import com.example.model.Order;
import com.example.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class OrderService extends MainService<Order> {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void addOrder(Order order) {
        if(order==null)
            throw new IllegalArgumentException("Order cannot be null");
        if(order.getId()==null)
            throw new IllegalArgumentException("Order ID cannot be null");
        if(orderRepository.getOrderById(order.getId())!=null)
            throw new IllegalArgumentException("Order already exists");
        orderRepository.addOrder(order);
    }

    public ArrayList<Order> getOrders() {
        return orderRepository.getOrders();
    }

    public Order getOrderById(UUID orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if(order==null)
            throw new IllegalArgumentException("Order not found");
        return order;
    }

    public void deleteOrderById(UUID orderId) throws IllegalArgumentException {
        Order order = orderRepository.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found.");
        }
        orderRepository.deleteOrderById(orderId);
    }

}
