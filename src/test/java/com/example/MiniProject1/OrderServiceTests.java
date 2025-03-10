package com.example.MiniProject1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.model.Order;
import com.example.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class OrderServiceTests {

    @Value("${spring.application.orderDataPath}")
    private String orderDataPath;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;



    @BeforeEach
    public void overRideAll() {
        try {
//            objectMapper.writeValue(new File(userDataPath), new ArrayList<User>());
//            objectMapper.writeValue(new File(productDataPath), new ArrayList<Product>());
            objectMapper.writeValue(new File(orderDataPath), new ArrayList<Order>());
//            objectMapper.writeValue(new File(cartDataPath), new ArrayList<Cart>());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }


    public Order addOrder(Order order){
          try{
                   File file = new File(orderDataPath);
                   ArrayList<Order> orders;
                   if (!file.exists()) {
                  orders = new ArrayList<>();
                   }
                   else {
                  orders = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Order[].class)));
                   }
                   orders.add(order);
                   objectMapper.writeValue(file, orders);
                   return order;
             } catch (IOException e) {
                   throw new RuntimeException("Failed to write to JSON file", e);
          }
     }

     public ArrayList<Order> getOrders() {
          try {
               File file = new File(orderDataPath);
               if (!file.exists()) {
                 return new ArrayList<>();
               }
               return new ArrayList<Order>(Arrays.asList(objectMapper.readValue(file, Order[].class)));
          } catch (IOException e) {
               throw new RuntimeException("Failed to read from JSON file", e);
          }
     }

    // Test 1: Happy Path – Adding a valid order should cause it to appear in the JSON file.
    @Test
    void testAddOrder_HappyPath() {
        Order order = new Order();
        UUID orderId = UUID.randomUUID();
        order.setId(orderId);
        order.setUserId(UUID.randomUUID());
        order.setTotalPrice(100.0);
        order.setProducts(new ArrayList<>());

        orderService.addOrder(order);

        ArrayList<Order> orders = getOrders();
        boolean found = orders.stream().anyMatch(o -> o.getId().equals(orderId));
        assertTrue(found, "Order should be present in the JSON file after addOrder");
    }

    // Test 2: Duplicate – Adding two orders with the same ID should produce two entries.
    @Test
    void testAddOrder_Duplicate() {
        UUID orderId = UUID.randomUUID();
        Order order1 = new Order();
        order1.setId(orderId);
        order1.setUserId(UUID.randomUUID());
        order1.setTotalPrice(100.0);
        order1.setProducts(new ArrayList<>());

        Order order2 = new Order();
        order2.setId(orderId);
        order2.setUserId(UUID.randomUUID());
        order2.setTotalPrice(100.0);
        order2.setProducts(new ArrayList<>());

        orderService.addOrder(order1);
        orderService.addOrder(order2);

        ArrayList<Order> orders = getOrders();
        long count = orders.stream().filter(o -> o.getId().equals(orderId)).count();
        assertEquals(1, count, "There should be one orders only no duplicates");
    }

    // Test 3: Invalid Input – Passing null to addOrder should leave the file unchanged.
    @Test
    void testAddOrder_InvalidInput() {
        try {
            orderService.addOrder(null);
        } catch (Exception e) {
            // If an exception is thrown, fail the test.
            fail("addOrder(null) should not throw an exception, but rather leave the file unchanged");
        }
        ArrayList<Order> orders = getOrders();
        assertEquals(0, orders.size(), "JSON file should remain empty when null is added");
    }

    // ---------------- Tests for getOrders() ----------------

    // Test 1: Happy Path – After adding orders, getOrders should return them.
    @Test
    void testGetOrders_HappyPath() {
        Order order1 = new Order();
        order1.setId(UUID.randomUUID());
        order1.setUserId(UUID.randomUUID());
        order1.setTotalPrice(50.0);
        order1.setProducts(new ArrayList<>());

        Order order2 = new Order();
        order2.setId(UUID.randomUUID());
        order2.setUserId(UUID.randomUUID());
        order2.setTotalPrice(75.0);
        order2.setProducts(new ArrayList<>());

        addOrder(order1);
        addOrder(order2);

        ArrayList<Order> orders = orderService.getOrders();
        assertEquals(2, orders.size(), "getOrders should return 2 orders");
    }

    // Test 2: Empty – When no orders are added, getOrders should return an empty list.
    @Test
    void testGetOrders_Empty() {
        ArrayList<Order> orders = orderService.getOrders();
        assertTrue(orders.isEmpty(), "getOrders should return an empty list when no orders are added");
    }

    // Test 3: After Deletion – After adding and then deleting an order, getOrders should return an empty list.
    @Test
    void testGetOrders_AfterDeletion() {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setUserId(UUID.randomUUID());
        order.setTotalPrice(60.0);
        order.setProducts(new ArrayList<>());

        addOrder(order);
        overRideAll();

        ArrayList<Order> orders = orderService.getOrders();
        assertTrue(orders.isEmpty(), "getOrders should return an empty list after deletion");
    }

    // ---------------- Tests for getOrderById(UUID orderId) ----------------

    // Test 1: Happy Path – After adding an order, getOrderById should retrieve it.
    @Test
    void testGetOrderById_HappyPath() {
        Order order = new Order();
        UUID orderId = UUID.randomUUID();
        order.setId(orderId);
        order.setUserId(UUID.randomUUID());
        order.setTotalPrice(100.0);
        order.setProducts(new ArrayList<>());

        addOrder(order);
        Order retrieved = orderService.getOrderById(orderId);
        assertNotNull(retrieved, "Order should be retrievable by its ID");
        assertEquals(orderId, retrieved.getId(), "Retrieved order ID should match");
    }

    // Test 2: Non-Existent – Retrieving an order with a random ID should return null.
    @Test
    void testGetOrderById_NonExistent() {
        Order retrieved = orderService.getOrderById(UUID.randomUUID());
        assertNull(retrieved, "Non-existent order should return null");
    }

    // Test 3: After Deletion – After deleting an order, getOrderById should return null.
    @Test
    void testGetOrderById_AfterDeletion() {
        Order order = new Order();
        UUID orderId = UUID.randomUUID();
        order.setId(orderId);
        order.setUserId(UUID.randomUUID());
        order.setTotalPrice(100.0);
        order.setProducts(new ArrayList<>());

        addOrder(order);
        overRideAll();
        Order retrieved = orderService.getOrderById(orderId);
        assertNull(retrieved, "Order should not be retrievable after deletion");
    }

    // ---------------- Tests for deleteOrderById(UUID orderId) ----------------

    // Test 1: Happy Path – Deleting an existing order removes it.
    @Test
    void testDeleteOrderById_HappyPath() {
        Order order = new Order();
        UUID orderId = UUID.randomUUID();
        order.setId(orderId);
        order.setUserId(UUID.randomUUID());
        order.setTotalPrice(150.0);
        order.setProducts(new ArrayList<>());

        addOrder(order);
        orderService.deleteOrderById(orderId);

        ArrayList<Order> orders = getOrders();
        orders.stream().filter(o -> o.getId().equals(orderId)).findAny().ifPresent(o -> {
            fail("Order should be deleted and not present in the JSON file");
        });

    }

    // Test 2: Non-Existent – Deleting a non-existent order should throw an IllegalArgumentException.
    @Test
    void testDeleteOrderById_NonExistent() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.deleteOrderById(UUID.randomUUID());
        });
        assertNotNull(exception.getMessage(), "An exception message should be provided");
    }

    // Test 3: Multiple Orders – After adding two orders and deleting one, the other should remain.
    @Test
    void testDeleteOrderById_MultipleOrders() {
        Order order1 = new Order();
        order1.setId(UUID.randomUUID());
        order1.setUserId(UUID.randomUUID());
        order1.setTotalPrice(150.0);
        order1.setProducts(new ArrayList<>());

        Order order2 = new Order();
        order2.setId(UUID.randomUUID());
        order2.setUserId(UUID.randomUUID());
        order2.setTotalPrice(250.0);
        order2.setProducts(new ArrayList<>());

        addOrder(order1);
        addOrder(order2);

        orderService.deleteOrderById(order1.getId());
        ArrayList<Order> orders = getOrders();
        assertEquals(1, orders.size(), "Only one order should remain after deletion");
        assertEquals(order2.getId(), orders.getFirst().getId(), "The remaining order should be order2");
    }
}
