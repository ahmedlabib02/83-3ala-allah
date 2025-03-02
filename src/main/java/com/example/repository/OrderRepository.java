package com.example.repository;

import com.example.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class OrderRepository extends MainRepository<Order> {

    public static List<Order> orders = new ArrayList<>();
    private static final String DATA_PATH = "data/orders.json";

    public OrderRepository() {
    }

    @Override
    protected String getDataPath() {
        return DATA_PATH;
    }

    @Override
    protected Class<Order[]> getArrayType() {
        return Order[].class;
    }

    public void addOrder(Order order){
        orders.add(order);
    }

    public ArrayList<Order> getOrders(){
        return new ArrayList<>(orders);
    }

    public Order getOrderById(UUID orderId) {
        return orders.stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    public void deleteOrderById(UUID orderId) {
        orders.removeIf(order -> order.getId().equals(orderId));
    }
}