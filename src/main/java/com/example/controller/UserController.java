package com.example.controller;

import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;
    CartService cartService;
    ProductService productService;

    @Autowired
    public UserController(UserService userService, CartService cartService, ProductService productService) {
        this.userService = userService;
        this.cartService = cartService;
        this.productService = productService;
    }

    /**
     * Adds a new user.
     *
     * @param user The user object to be added.
     * @return The created user.
     */
    @PostMapping("/")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     */
    @GetMapping("/")
    public ArrayList<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * Retrieves a specific user by their ID.
     *
     * @param userId The unique identifier of the user.
     * @return The user object if found.
     */
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

    /**
     * Retrieves all orders of a specific user.
     *
     * @param userId The unique identifier of the user.
     * @return A list of orders belonging to the user.
     */
    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId) {
        return userService.getOrdersByUserById(userId);
    }

    /**
     * Creates a new order for a specific user.
     *
     * @param userId The unique identifier of the user.
     * @return HTTP status indicating success.
     */
    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId) {
        userService.addOrderToUser(userId);
        return HttpStatus.OK.toString();
    }

    /**
     * Removes a specific order from a user.
     *
     * @param userId  The unique identifier of the user.
     * @param orderId The unique identifier of the order to be removed.
     * @return HTTP status indicating success.
     */
    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId) {
        userService.removeOrderFromUser(userId, orderId);
        return HttpStatus.OK.toString();
    }

    /**
     * Empties the cart of a specific user.
     *
     * @param userId The unique identifier of the user.
     * @return HTTP status indicating success.
     */
    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId) {
        userService.emptyCart(userId);
        return HttpStatus.OK.toString();
    }

    /**
     * Adds a product to a user's cart.
     *
     * @param userId    The unique identifier of the user.
     * @param productId The unique identifier of the product.
     * @return HTTP status indicating success.
     */
    @PostMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        Product product = productService.getProductById(productId);
        cartService.addProductToCart(userId, product);
        return HttpStatus.OK.toString();
    }

    /**
     * Removes a specific product from a user's cart.
     *
     * @param userId    The unique identifier of the user.
     * @param productId The unique identifier of the product.
     * @return HTTP status indicating success.
     */
    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        Product product = productService.getProductById(productId);
        cartService.deleteProductFromCart(userId, product);
        return HttpStatus.OK.toString();
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId The unique identifier of the user.
     * @return HTTP status indicating success.
     */
    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId) {
        userService.deleteUserById(userId);
        return HttpStatus.OK.toString();
    }
}
