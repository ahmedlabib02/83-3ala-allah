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
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Add a new user.
     * */
    @PostMapping("/")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * Get all users.
     * */
    @GetMapping("/")
    public ArrayList<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * Get a specifc user by passing his/her ID in the URL.
     * */
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

    /**
     * Get the orders of a specific user by passing his/her ID in the URL.
     * */
    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId) {
        return userService.getOrdersByUserById(userId);
    }

    /**
     * Issue a new order for the user.
     * */
    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId) {
        userService.addOrderToUser(userId);
        return HttpStatus.OK.toString();
    }

    /**
     * Remove a specific order from the user.
     * */
    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID orderId) {
        userService.removeOrderFromUser(userId, orderId);
        return HttpStatus.OK.toString();
    }

    /**
     * Empty the cart of the user.
     * */
    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId) {
        userService.emptyCart(userId);
        return HttpStatus.OK.toString();
    }

    /**
     * Add a specific product to the cart by passing their IDs in the request body.
     * */
    @PostMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        Product product = productService.getProductById(productId);
        cartService.addProductToCart(userId, product);
        return HttpStatus.OK.toString();
    }

    /**
     * Delete a specific product from the cart.
     * */
    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        Product product = productService.getProductById(productId);
        cartService.deleteProductFromCart(userId, product);
        return HttpStatus.OK.toString();
    }

    /**
     * Delete a specific user.
     * */
    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId) {
        userService.deleteUserById(userId);
        return HttpStatus.OK.toString();
    }
}
