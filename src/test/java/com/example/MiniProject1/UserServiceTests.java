//package com.example.MiniProject1;
//
//
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.example.model.Cart;
//import com.example.model.Order;
//import com.example.model.Product;
//import com.example.model.User;
//import com.example.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ComponentScan(basePackages = "com.example.*")
//@SpringBootTest
//public class UserServiceTests {
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
//
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
//
//    @Test
//    void testAddUser_HappyPath() {
//        UUID userId = UUID.randomUUID();
//        User user = new User();
//        user.setId(userId);
//        user.setName("Test User Happy");
//
//        User returned = userService.addUser(user);
//        assertNotNull(returned, "addUser should return a non-null user");
//        assertEquals(userId, returned.getId(), "Returned user ID should match");
//        assertEquals("Test User Happy", returned.getName(), "Returned user name should match");
//
//        // Verify directly from the JSON file using the helper method.
//        List<User> users = getUsers(); // Helper method that reads the user JSON file.
//        boolean found = users.stream()
//                .anyMatch(u -> u.getId().equals(userId) && "Test User Happy".equals(u.getName()));
//        assertTrue(found, "The user should be present in the JSON file after addUser");
//    }
//
//
//    @Test
//    void testAddUser_Duplicate() {
//        UUID userId = UUID.randomUUID();
//        User user1 = new User();
//        user1.setId(userId);
//        user1.setName("Duplicate User");
//
//        User user2 = new User();
//        user2.setId(userId);
//        user2.setName("Duplicate User");
//        userService.addUser(user1);
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            userService.addUser(user2);
//                }, "You cannot add duplicate users");
//
//        List<User> users = getUsers();
//        long count = users.stream().filter(u -> u.getId().equals(userId)).count();
//
//    }
//
//
//    @Test
//    void testAddUser_InvalidInput() {
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            userService.addUser(null);
//        });
//        assertEquals("user is null", exception.getMessage(), "Exception message should be 'user is null'");
//    }
//
//
//    @Test
//    public void testGetUsers_HappyPath() {
//        // Arrange: Add two users directly to the JSON file.
//        User user1 = new User();
//        user1.setId(UUID.randomUUID());
//        user1.setName("Alice");
//
//        User user2 = new User();
//        user2.setId(UUID.randomUUID());
//        user2.setName("Bob");
//
//        addUser(user1);
//        addUser(user2);
//
//        // Act: Retrieve all users.
//        ArrayList<User> users = userService.getUsers();
//
//        // Assert: Verify that both users are returned.
//        assertEquals(2, users.size(), "getUsers should return two users");
//    }
//
//    @Test
//    public void testGetUsers_Empty() {
//        // Act: Retrieve users when no user has been added.
//        ArrayList<User> users = userService.getUsers();
//
//        // Assert: The returned list should be empty.
//        assertTrue(users.isEmpty(), "getUsers should return an empty list when no users are added");
//    }
//
//    @Test
//    public void testGetUsers_AfterDeletion() {
//        // Arrange: Add two users and then delete one.
//        User user1 = new User();
//        user1.setId(UUID.randomUUID());
//        user1.setName("Charlie");
//
//        User user2 = new User();
//        user2.setId(UUID.randomUUID());
//        user2.setName("Dana");
//
//        addUser(user1);
//        addUser(user2);
//        userService.deleteUserById(user1.getId());
//
//        // Act: Retrieve users after deletion.
//        ArrayList<User> users = userService.getUsers();
//
//        // Assert: Only the non-deleted user should remain.
//        assertEquals(1, users.size(), "getUsers should return one user after deletion");
//        assertEquals(user2.getId(), users.get(0).getId(), "The remaining user should be Dana");
//    }
//
//
//
//
//    @Test
//    void testGetUserById_HappyPath() {
//
//        UUID userId = UUID.randomUUID();
//        User expectedUser = new User();
//        expectedUser.setId(userId);
//        expectedUser.setName("Happy Path User");
//
//        // Use the helper method to add the user directly to the JSON file.
//        addUser(expectedUser);
//
//        // Now call the service method to retrieve the user.
//        User retrieved = userService.getUserById(userId);
//        assertNotNull(retrieved, "getUserById should retrieve the user when it exists");
//        assertEquals(expectedUser.getId(), retrieved.getId(), "Retrieved user ID should match the expected ID");
//        assertEquals(expectedUser.getName(), retrieved.getName(), "Retrieved user name should match the expected name");
//    }
//
//
//    @Test
//    void testGetUserById_NonExistent() {
//        UUID randomId = UUID.randomUUID();
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            userService.getUserById(randomId);
//        });
//        assertEquals("user not found", exception.getMessage(), "Exception message should be 'user is null'");
//    }
//
//
//    @Test
//    void testGetUserById_AfterDeletion() {
//
//        UUID userId = UUID.randomUUID();
//        User user = new User();
//        user.setId(userId);
//        user.setName("User To Delete");
//        addUser(user);
//        overRideAll();
//        Exception exception = assertThrows(IllegalArgumentException.class, ()->{
//            userService.getUserById(userId);
//        });
//        assertEquals("user not found", exception.getMessage(), "Exception message should be 'user not found'");
//
//    }
//
//
//
//    @Test
//    void testGetOrdersByUserId_HappyPath() {
//
//        UUID userId = UUID.randomUUID();
//        Order order = new Order();
//        order.setId(UUID.randomUUID());
//        order.setUserId(userId);
//        order.setTotalPrice(100.0);
//        order.setProducts(new ArrayList<>());
//        addOrder(order);
//        User user = new User();
//        user.setId(userId);
//        user.setName("Order User");
//        user.setOrders(new ArrayList<>(List.of(order)));
//        addUser(user);
//
//
//        List<Order> userOrders = userService.getOrdersByUserById(userId);
//        assertEquals(1, userOrders.size(), "User should have one order");
//
//    }
//
//
//    @Test
//    void testGetOrdersByUserId_Empty() {
//        // Create a user with an empty order list.
//        UUID userId = UUID.randomUUID();
//        User user = new User();
//        user.setId(userId);
//        user.setName("No Order User");
//        user.setOrders(new ArrayList<>());
//        addUser(user);
//        List<Order> userOrders = userService.getOrdersByUserById(userId);
//        assertTrue(userOrders.isEmpty(), "User with no orders should return an empty list");
//    }
//
//    @Test
//    void testGetOrdersByUserId_NonExistentUser() {
//
//        UUID randomUserId = UUID.randomUUID();
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            userService.getOrdersByUserById(randomUserId);
//        });
//        assertEquals("user not found", exception.getMessage(), "Exception message should be 'user not found'");
//    }
//
//
//
//    @Test
//    void testAddOrderToUser_SingleProduct() {
//        // Create and add a user using helper.
//        UUID userId = UUID.randomUUID();
//        User user = new User();
//        user.setId(userId);
//        user.setName("Single Product User");
//        addUser(user);
//
//        // Create a cart for the user with a single product.
//        Cart cart = new Cart();
//        cart.setId(UUID.randomUUID());
//        cart.setUserId(userId);
//        Product product = new Product(UUID.randomUUID(), "Product A", 50.0);
//        cart.setProducts(new ArrayList<>(List.of(product)));
//        addCart(cart);
//
//        userService.addOrderToUser(userId);
//
//        List<Order> orders = getOrders();
//        assertEquals(1, orders.size(), "One order should be created");
//        Order order = orders.getFirst();
//        assertEquals(userId, order.getUserId(), "Order should belong to the correct user");
//        assertEquals(50.0, order.getTotalPrice(), 0.001, "Order total should equal the product price");
//    }
//
//    @Test
//    void testAddOrderToUser_MultipleProducts() {
//        // Create and add a user.
//        UUID userId = UUID.randomUUID();
//        User user = new User();
//        user.setId(userId);
//        user.setName("Multiple Products User");
//        addUser(user);
//
//        // Create a cart with two products.
//        Cart cart = new Cart();
//        cart.setId(UUID.randomUUID());
//        cart.setUserId(userId);
//        Product product1 = new Product(UUID.randomUUID(), "Product A", 30.0);
//        Product product2 = new Product(UUID.randomUUID(), "Product B", 20.0);
//        cart.setProducts(new ArrayList<>(List.of(product1, product2)));
//        addCart(cart);
//
//        // Call addOrderToUser.
//        userService.addOrderToUser(userId);
//
//        // Verify the order total equals 50.0 by reading the orders JSON file.
//        List<Order> orders = getOrders();
//        assertEquals(1, orders.size(), "One order should be created");
//        Order order = orders.get(0);
//        assertEquals(50.0, order.getTotalPrice(), 0.001, "Order total should equal the sum of product prices");
//    }
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
//        Product product = new Product(UUID.randomUUID(), "Product A", 40.0);
//        cart.setProducts(new ArrayList<>(List.of(product)));
//        addCart(cart);
//        userService.addOrderToUser(userId);
//        List<Order> orders = getOrders();
//        assertEquals(1, orders.size(), "One order should be created");
//
//
//        List<Cart> carts = getCarts();
//        Cart userCart = carts.stream().filter(c -> c.getUserId().equals(userId)).findFirst().orElse(null);
//        assertNotNull(userCart, "User's cart should exist");
//        assertTrue(userCart.getProducts().isEmpty(), "User's cart should be empty after checkout");
//    }
//
//
//    // Test 1: Happy Path – Given a user with a non-empty cart, emptyCart should clear the products.
//    @Test
//    void testEmptyCart_HappyPath() {
//        UUID userId = UUID.randomUUID();
//
//        User user = new User();
//        user.setId(userId);
//        user.setName("Cart User");
//        addUser(user);
//        Cart cart = new Cart();
//        cart.setId(UUID.randomUUID());
//        cart.setUserId(userId);
//        Product product = new Product(UUID.randomUUID(), "Product A", 25.0);
//        cart.setProducts(new ArrayList<>(List.of(product)));
//        addCart(cart);
//        userService.emptyCart(userId);
//        List<Cart> carts = getCarts();
//        Cart userCart = carts.stream().filter(c -> c.getUserId().equals(userId)).findFirst().orElse(null);
//        assertNotNull(userCart, "User's cart should exist");
//        assertTrue(userCart.getProducts().isEmpty(), "User's cart should be empty after emptyCart");
//    }
//
//
//    // Test 2: Non-Existent User – Calling emptyCart on a user not present should leave carts unchanged.
//    @Test
//    void testEmptyCart_NonExistentUser() {
//        UUID randomUserId = UUID.randomUUID();
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            userService.emptyCart(randomUserId);
//        });
//        assertEquals("user not found", exception.getMessage(), "Exception message should be 'user not found'");
//    }
//
//    // Test 3: Already Empty – If the user's cart is already empty, emptyCart should leave it empty.
//    @Test
//    void testEmptyCart_AlreadyEmpty() {
//        UUID userId = UUID.randomUUID();
//        User user = new User();
//        user.setId(userId);
//        user.setName("Already Empty User");
//        addUser(user);
//
//        // Create a cart with no products.
//        Cart cart = new Cart();
//        cart.setId(UUID.randomUUID());
//        cart.setUserId(userId);
//        cart.setProducts(new ArrayList<>());
//        addCart(cart);
//
//        userService.emptyCart(userId);
//        List<Cart> carts = getCarts();
//        Cart userCart = carts.stream().filter(c -> c.getUserId().equals(userId)).findFirst().orElse(null);
//        assertNotNull(userCart, "User's cart should exist");
//        assertTrue(userCart.getProducts().isEmpty(), "Cart should remain empty after emptyCart is called on an already empty cart");
//    }
//
//
//
//    @Test
//    void testRemoveOrderFromUser_HappyPath() {
//        UUID userId = UUID.randomUUID();
//        User user = new User();
//        user.setId(userId);
//        user.setName("Order Removal User");
//
//        Order order = new Order();
//        UUID orderId = UUID.randomUUID();
//        order.setId(orderId);
//        order.setUserId(userId);
//        order.setTotalPrice(80.0);
//        order.setProducts(new ArrayList<>());
//        user.setOrders(new ArrayList<>(List.of(order)));
//        addUser(user);
//        addOrder(order);
//
//
//        userService.removeOrderFromUser(userId, orderId);
//
//        List<User> users = getUsers();
//        User foundUser = users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
//        assertNotNull(foundUser, "User should exist");
//        // The user's orders list should be empty.
//        assertTrue(foundUser.getOrders() == null || foundUser.getOrders().isEmpty(),
//                "User's order list should be empty after removal");
//    }
//
//    // Test 2: Order Not Found – If the specified orderId is not in the user's order list, no removal occurs.
//    @Test
//    void testRemoveOrderFromUser_OrderNotFound() {
//        UUID userId = UUID.randomUUID();
//        User user = new User();
//        user.setId(userId);
//        user.setName("Order Not Found User");
//        Order order = new Order();
//        order.setId(UUID.randomUUID());
//        order.setUserId(userId);
//        order.setTotalPrice(80.0);
//        order.setProducts(new ArrayList<>());
//        user.setOrders(new ArrayList<>(List.of(order)));
//        addUser(user);
//
//
//        Exception exception = assertThrows(
//                IllegalArgumentException.class, ()->{userService.removeOrderFromUser(userId, UUID.randomUUID());});
//
//
//        assertEquals("order not found", exception.getMessage(), "Exception message should be 'order not found'");
//    }
//
//    // Test 3: Non-Existent User – Removing an order for a user not in the system should leave orders unchanged.
//    @Test
//    void testRemoveOrderFromUser_NonExistentUser() {
//        UUID randomUserId = UUID.randomUUID();
//        Exception exception = assertThrows(
//                IllegalArgumentException.class, ()->{
//            userService.removeOrderFromUser(randomUserId, UUID.randomUUID());
//        });
//        assertEquals("user not found", exception.getMessage(), "Exception message should be 'user not found'");
//
//    }
//
//    // =============================================================
//    // 3. Tests for deleteUserById(UUID userId)
//    // =============================================================
//
//    // Test 1: Happy Path – A user is deleted and is no longer present in the JSON file.
//    @Test
//    void testDeleteUserById_HappyPath() {
//        UUID userId = UUID.randomUUID();
//        User user = new User();
//        user.setId(userId);
//        user.setName("Delete User");
//        addUser(user);
//
//        userService.deleteUserById(userId);
//
//        List<User> users = getUsers();
//        boolean found = users.stream().anyMatch(u -> u.getId().equals(userId));
//        assertFalse(found, "User should be deleted from the JSON file");
//    }
//
//    @Test
//    void testDeleteUserById_NonExistent_ExistingRemains() {
//        // Add a user
//        UUID existingUserId = UUID.randomUUID();
//        User user = new User();
//        user.setId(existingUserId);
//        user.setName("Existing User");
//        addUser(user);
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            userService.deleteUserById(UUID.randomUUID());
//        });
//
//        assertEquals("user not found", exception.getMessage(), "Exception message should be 'user not found'");
//
//
//    }
//
//    // Test 3: Multiple Users – When multiple users exist, deleting one should leave the other intact.
//    // Test 3: Multiple Users – When multiple users exist, deleting one should leave the other intact.
//    @Test
//    void testDeleteUserById_MultipleUsers() {
//        User user1 = new User();
//        user1.setId(UUID.randomUUID());
//        user1.setName("User One");
//        User user2 = new User();
//        user2.setId(UUID.randomUUID());
//        user2.setName("User Two");
//        addUser(user1);
//        addUser(user2);
//        userService.deleteUserById(user1.getId());
//
//        List<User> users = getUsers();
//        assertEquals(1, users.size(), "Only one user should remain after deletion");
//        assertEquals(user2.getId(), users.get(0).getId(), "Remaining user should be user2");
//    }
//
//
//
//
//}