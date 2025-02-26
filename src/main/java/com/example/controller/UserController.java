package com.example.controller;

import com.example.model.Order;
import com.example.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * Add a new user.
     * */
    @PostMapping("/")
    public User addUser(@RequestBody User user) {
        return null;
    }

    /**
     * Get all users.
     * */
    @GetMapping("/")
    public ArrayList<User> getUsers() {
        return null;
    }

    /**
     * Get a specifc user by passing his/her ID in the URL.
     * */
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable UUID userId) {
        return null;
    }

    /**
     * Get the orders of a specific user by passing his/her ID in the URL.
     * */
    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId) {
        return null;
    }

    /**
     * Issue a new order for the user.
     * */
    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId) {
        return null;
    }

    /**
     * Remove a specific order from the user.
     * */
    @PostMapping("/{userId}/removeOrder")
    public String removeOrderFromUser(@PathVariable UUID userId, @RequestParam UUID order) {
        return null;
    }

    /**
     * Empty the cart of the user.
     * */
    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId) {
        return null;
    }

    /**
     * Add a specific product to the cart by passing their IDs in the request body.
     * */
    @PostMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        return null;
    }

    /**
     * Delete a specific product from the cart.
     * */
    @PutMapping("/deleteProductFromCart")
    public String deleteProductFromCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        return null;
    }

    /**
     * Delete a specific user.
     * */
    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId) {
        return null;
    }

}
