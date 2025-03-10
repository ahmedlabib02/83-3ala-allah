//package com.example.MiniProject1;
//
//import com.example.model.Cart;
//import com.example.model.Order;
//import com.example.model.Product;
//import com.example.model.User;
//import com.example.service.CartService;
//import com.example.service.UserService;
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
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class TempTests {
//
//    @Value("${spring.application.userDataPath}")
//    private String userDataPath;
//
//    @Value("${spring.application.productDataPath}")
//    private String productDataPath;
//
//    @Value("${spring.application.orderDataPath}")
//    private String orderDataPath;
//
//    @Value("${spring.application.cartDataPath}")
//    private String cartDataPath;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private UserService userService;
//
//
//
//
//    @Autowired
//    private CartService cartService;
//
//
//
//
//    // ------------------ Helper Methods Provided ------------------
//    @BeforeEach
//    public void overRideAll() {
//        try {
//            objectMapper.writeValue(new File(userDataPath), new ArrayList<User>());
//            objectMapper.writeValue(new File(productDataPath), new ArrayList<Product>());
//            objectMapper.writeValue(new File(orderDataPath), new ArrayList<Order>());
//            objectMapper.writeValue(new File(cartDataPath), new ArrayList<Cart>());
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to write to JSON file", e);
//        }
//    }
//
//    public Object find(String typeString, Object toFind) {
//        switch (typeString) {
//            case "User":
//                ArrayList<User> users = getUsers();
//                for (User user : users) {
//                    if (user.getId().equals(((User) toFind).getId())) {
//                        return user;
//                    }
//                }
//                break;
//            case "Product":
//                ArrayList<Product> products = getProducts();
//                for (Product product : products) {
//                    if (product.getId().equals(((Product) toFind).getId())) {
//                        return product;
//                    }
//                }
//                break;
//            case "Order":
//                ArrayList<Order> orders = getOrders();
//                for (Order order : orders) {
//                    if (order.getId().equals(((Order) toFind).getId())) {
//                        return order;
//                    }
//                }
//                break;
//            case "Cart":
//                ArrayList<Cart> carts = getCarts();
//                for (Cart cart : carts) {
//                    if (cart.getId().equals(((Cart) toFind).getId())) {
//                        return cart;
//                    }
//                }
//                break;
//        }
//        return null;
//    }
//
//    public Product addProduct(Product product) {
//        try {
//            File file = new File(productDataPath);
//            ArrayList<Product> products;
//            if (!file.exists()) {
//                products = new ArrayList<>();
//            } else {
//                products = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Product[].class)));
//            }
//            products.add(product);
//            objectMapper.writeValue(file, products);
//            return product;
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to write to JSON file", e);
//        }
//    }
//
//    public ArrayList<Product> getProducts() {
//        try {
//            File file = new File(productDataPath);
//            if (!file.exists()) {
//                return new ArrayList<>();
//            }
//            return new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Product[].class)));
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to read from JSON file", e);
//        }
//    }
//
//    public User addUser(User user) {
//        try {
//            File file = new File(userDataPath);
//            ArrayList<User> users;
//            if (!file.exists()) {
//                users = new ArrayList<>();
//            } else {
//                users = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, User[].class)));
//            }
//            users.add(user);
//            objectMapper.writeValue(file, users);
//            return user;
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to write to JSON file", e);
//        }
//    }
//
//    public ArrayList<User> getUsers() {
//        try {
//            File file = new File(userDataPath);
//            if (!file.exists()) {
//                return new ArrayList<>();
//            }
//            return new ArrayList<>(Arrays.asList(objectMapper.readValue(file, User[].class)));
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to read from JSON file", e);
//        }
//    }
//
//    public Cart addCart(Cart cart) {
//        try {
//            File file = new File(cartDataPath);
//            ArrayList<Cart> carts;
//            if (!file.exists()) {
//                carts = new ArrayList<>();
//            } else {
//                carts = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Cart[].class)));
//            }
//            carts.add(cart);
//            objectMapper.writeValue(file, carts);
//            return cart;
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to write to JSON file", e);
//        }
//    }
//
//    public ArrayList<Cart> getCarts() {
//        try {
//            File file = new File(cartDataPath);
//            if (!file.exists()) {
//                return new ArrayList<>();
//            }
//            return new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Cart[].class)));
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to read from JSON file", e);
//        }
//    }
//
//    public Order addOrder(Order order) {
//        try {
//            File file = new File(orderDataPath);
//            ArrayList<Order> orders;
//            if (!file.exists()) {
//                orders = new ArrayList<>();
//            } else {
//                orders = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Order[].class)));
//            }
//            orders.add(order);
//            objectMapper.writeValue(file, orders);
//            return order;
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to write to JSON file", e);
//        }
//    }
//
//    public ArrayList<Order> getOrders() {
//        try {
//            File file = new File(orderDataPath);
//            if (!file.exists()) {
//                return new ArrayList<>();
//            }
//            return new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Order[].class)));
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to read from JSON file", e);
//        }
//    }
//
//
//
//
//    @Test
//    void testAddOrderToUser_CartEmptiedAfterCheckout() {
//        // Create and add a user.
//        UUID userId = UUID.randomUUID();
//        User user = new User();
//        user.setId(userId);
//        user.setName("Cart Emptied User");
//        addUser(user);
//
//        // Create a cart with a product.
//        Cart cart = new Cart();
//        cart.setId(UUID.randomUUID());
//        cart.setUserId(userId);
//
//        Product product = new Product(UUID.randomUUID(), "Product A", 40.0);
//        cart.setProducts(new ArrayList<>(List.of(product)));
//        addCart(cart);
//
//
//        userService.addOrderToUser(userId);
//
//
//        List<Order> orders = getOrders();
//        assertEquals(1, orders.size(), "One order should be created");
//
//
//        List<Cart> carts = getCarts();
//        Cart userCart = carts.stream().filter(c -> c.getUserId().equals(userId)).findFirst().orElse(null);
//        assertNotNull(userCart, "User's cart should exist");
//        assertTrue(userCart.getProducts().isEmpty(), "User's cart should be empty after checkout");
//    }
//}
