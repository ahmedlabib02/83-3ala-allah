//package com.example.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.example.model.Order;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@SpringBootTest
//public class OrderServiceTests {
//
//    @Value("${spring.application.orderDataPath}")
//    private String orderDataPath;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    /**
//     * Clears the order JSON file before each test.
//     */
//    @BeforeEach
//    public void setUp() {
//        File file = new File(orderDataPath);
//        try {
//            objectMapper.writeValue(file, new ArrayList<Order>());
//        } catch (IOException e) {
//            fail("Failed to clear the order data file: " + e.getMessage());
//        }
//    }
//
//    // ============================================================
//    // Tests for addOrder() method (3 tests)
//    // ============================================================
//
//    @Test
//    public void testAddOrder_HappyPath() {
//        UUID orderId = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//        Order order = new Order();
//        order.setId(orderId);
//        order.setUserId(userId);
//        order.setTotalPrice(150.0);
//        order.setProducts(new ArrayList<>());
//        Order returnedOrder = orderService.addOrder(order);
//
//        assertNotNull(returnedOrder, "Order should not be null after addition");
//        assertEquals(orderId, returnedOrder.getId(), "Returned order ID should match the provided ID");
//
//        // Verify that the JSON file contains this order.
//        try {
//            Order[] orders = objectMapper.readValue(new File(orderDataPath), Order[].class);
//            boolean found = false;
//            for (Order o : orders) {
//                if (o.getId().equals(orderId)) {
//                    found = true;
//                    break;
//                }
//            }
//            assertTrue(found, "Order should be present in the JSON file after addition");
//        } catch (IOException e) {
//            fail("Error reading order JSON file: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testAddOrder_Duplicate() {
//        // Since duplicates are not handled, adding two orders with the same ID will result in two entries.
//        UUID orderId = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//        Order order1 = new Order();
//        order1.setId(orderId);
//        order1.setUserId(userId);
//        order1.setTotalPrice(150.0);
//        order1.setProducts(new ArrayList<>());
//        Order order2 = new Order();
//        order2.setId(orderId);
//        order2.setUserId(userId);
//        order2.setTotalPrice(150.0);
//        order2.setProducts(new ArrayList<>());
//        orderService.addOrder(order1);
//        orderService.addOrder(order2);
//
//        try {
//            Order[] orders = objectMapper.readValue(new File(orderDataPath), Order[].class);
//            int count = 0;
//            for (Order o : orders) {
//                if (o.getId().equals(orderId)) {
//                    count++;
//                }
//            }
//            assertEquals(2, count, "There should be two orders with the same ID in the JSON file");
//        } catch (IOException e) {
//            fail("Error reading order JSON file: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testAddOrder_InvalidInput() {
//        // When a null order is passed, assume the service returns null and leaves the JSON file unchanged.
//        Order result = orderService.addOrder(null);
//        assertNull(result, "addOrder(null) should return null");
//        try {
//            Order[] orders = objectMapper.readValue(new File(orderDataPath), Order[].class);
//            assertEquals(0, orders.length, "JSON file should remain empty when null is added");
//        } catch (IOException e) {
//            fail("Error reading order JSON file: " + e.getMessage());
//        }
//    }
//
//    // ============================================================
//    // Tests for getOrders() method (3 tests)
//    // ============================================================
//
//    @Test
//    public void testGetOrders_HappyPath() {
//        Order order1 = new Order();
//        order1.setId(UUID.randomUUID());
//        order1.setUserId(UUID.randomUUID());
//        order1.setTotalPrice(100.0);
//        order1.setProducts(new ArrayList<>());
//        Order order2 = new Order();
//        order2.setId(UUID.randomUUID());
//        order2.setUserId(UUID.randomUUID());
//        order2.setTotalPrice(200.0);
//        order2.setProducts(new ArrayList<>());
//        orderService.addOrder(order1);
//        orderService.addOrder(order2);
//
//        ArrayList<Order> orders = orderService.getOrders();
//        assertEquals(2, orders.size(), "There should be 2 orders returned");
//    }
//
//    @Test
//    public void testGetOrders_Empty() {
//        ArrayList<Order> orders = orderService.getOrders();
//        assertTrue(orders.isEmpty(), "getOrders should return an empty list when no orders are added");
//    }
//
//    @Test
//    public void testGetOrders_AfterDeletion() {
//        Order order = new Order();
//        order.setId(UUID.randomUUID());
//        order.setUserId(UUID.randomUUID());
//        order.setTotalPrice(100.0);
//        order.setProducts(new ArrayList<>());
//        orderService.addOrder(order);
//        orderService.deleteOrderById(order.getId());
//
//        ArrayList<Order> orders = orderService.getOrders();
//        assertTrue(orders.isEmpty(), "getOrders should return an empty list after deletion");
//    }
//
//    // ============================================================
//    // Tests for getOrderById() method (3 tests)
//    // ============================================================
//
//    @Test
//    public void testGetOrderById_HappyPath() {
//        UUID orderId = UUID.randomUUID();
//        Order order = new Order();
//        order.setId(orderId);
//        order.setUserId(UUID.randomUUID());
//        order.setTotalPrice(150.0);
//        order.setProducts(new ArrayList<>());
//        orderService.addOrder(order);
//
//        Order retrieved = orderService.getOrderById(orderId);
//        assertNotNull(retrieved, "Order should be retrievable by its ID");
//        assertEquals(orderId, retrieved.getId(), "Retrieved order ID should match");
//    }
//
//    @Test
//    public void testGetOrderById_NonExistent() {
//        Order retrieved = orderService.getOrderById(UUID.randomUUID());
//        assertNull(retrieved, "Non-existent order ID should return null");
//    }
//
//    @Test
//    public void testGetOrderById_AfterDeletion() {
//        UUID orderId = UUID.randomUUID();
//        Order order = new Order();
//        order.setId(orderId);
//        order.setUserId(UUID.randomUUID());
//        order.setTotalPrice(150.0);
//        order.setProducts(new ArrayList<>());
//        orderService.addOrder(order);
//        orderService.deleteOrderById(orderId);
//
//        Order retrieved = orderService.getOrderById(orderId);
//        assertNull(retrieved, "Order should not be retrievable after deletion");
//    }
//
//    // ============================================================
//    // Tests for deleteOrderById() method (3 tests)
//    // ============================================================
//
//    @Test
//    public void testDeleteOrderById_HappyPath() {
//        UUID orderId = UUID.randomUUID();
//        Order order = new Order();
//        order.setId(orderId);
//        order.setUserId(UUID.randomUUID());
//        order.setTotalPrice(150.0);
//        order.setProducts(new ArrayList<>());
//        orderService.addOrder(order);
//        orderService.deleteOrderById(orderId);
//
//        Order retrieved = orderService.getOrderById(orderId);
//        assertNull(retrieved, "Order should be deleted and not retrievable");
//    }
//
//    @Test
//    public void testDeleteOrderById_NonExistent() {
//        // Deleting a non-existent order should leave the JSON file unchanged.
//        orderService.deleteOrderById(UUID.randomUUID());
//        try {
//            Order[] orders = objectMapper.readValue(new File(orderDataPath), Order[].class);
//            assertEquals(0, orders.length, "JSON file should remain empty when deleting a non-existent order");
//        } catch (IOException e) {
//            fail("Error reading order JSON file: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testDeleteOrderById_MultipleOrders() {
//        Order order1 = new Order();
//        order1.setId(UUID.randomUUID());
//        order1.setUserId(UUID.randomUUID());
//        order1.setTotalPrice(150.0);
//        order1.setProducts(new ArrayList<>());
//        Order order2 = new Order();
//        order2.setId(UUID.randomUUID());
//        order2.setUserId(UUID.randomUUID());
//        order2.setTotalPrice(250.0);
//        order2.setProducts(new ArrayList<>());
//        orderService.addOrder(order1);
//        orderService.addOrder(order2);
//
//        orderService.deleteOrderById(order1.getId());
//        ArrayList<Order> orders = orderService.getOrders();
//        assertEquals(1, orders.size(), "Only one order should remain after deletion");
//        assertEquals(order2.getId(), orders.get(0).getId(), "The remaining order should be order2");
//    }
//}
