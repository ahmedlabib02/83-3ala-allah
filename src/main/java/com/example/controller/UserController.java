package com.example.controller;

import com.example.model.Cart;
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
        try {
            return userService.addUser(user);
        } catch (Exception e) {
            return null;
        }
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
        try {
            return userService.getUserById(userId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves all orders of a specific user.
     *
     * @param userId The unique identifier of the user.
     * @return A list of orders belonging to the user.
     */
    @GetMapping("/{userId}/orders")
    public List<Order> getOrdersByUserId(@PathVariable UUID userId) {
        try {
            return userService.getOrdersByUserById(userId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Creates a new order for a specific user.
     *
     * @param userId The unique identifier of the user.
     * @return HTTP status indicating success.
     */
    @PostMapping("/{userId}/checkout")
    public String addOrderToUser(@PathVariable UUID userId) {
        try {
            userService.addOrderToUser(userId);
            return "Order added successfully";
        } catch (IllegalStateException e) {
            return "Cart is empty";
        } catch (IllegalArgumentException e) {
            return "User not found";
        } catch (Exception e) {
            return "Invalid request";
        }
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
        try {
            userService.removeOrderFromUser(userId, orderId);
            return "Order removed successfully";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("user not found")) {
                return "User not found";
            } else if (e.getMessage().equals("order not found")) {
                return "Order not found";
            } else if (e.getMessage().equals("user not assigned to that order")) {
                return "User is not assigned to that order";
            } else {
                return "Invalid request";
            }
        } catch (Exception e) {
            return "Invalid request";
        }
    }

    /**
     * Empties the cart of a specific user.
     *
     * @param userId The unique identifier of the user.
     * @return HTTP status indicating success.
     */
    @DeleteMapping("/{userId}/emptyCart")
    public String emptyCart(@PathVariable UUID userId) {
        try {
            userService.emptyCart(userId);
            return "Cart emptied successfully";
        } catch (Exception e) {
            return "User not found";
        }
    }

    /**
     * Adds a product to a user's cart.
     *
     * @param userId    The unique identifier of the user.
     * @param productId The unique identifier of the product.
     * @return HTTP status indicating success.
     */
    @PutMapping("/addProductToCart")
    public String addProductToCart(@RequestParam UUID userId, @RequestParam UUID productId) {
        try {
            Product product = null;
            Cart cart = null;

            try {
                product = productService.getProductById(productId);
            } catch (Exception e) {
                return "Product not found";
            }

            try {
                cart = cartService.getCartByUserId(userId);
            } catch (Exception e) {
                return "User not found";
            }

            // Create a new cart
            if (cart == null) {
                // Create product list
                ArrayList<Product> products = new ArrayList<>();
                // the cart contains only one product
                products.add(product);

                // Create new user cart
                cart = new Cart(UUID.randomUUID(), userId, products);


                // Add the new cart to the repository
                cartService.addCart(cart);
            }

            cartService.addProductToCart(cart.getId(), product);

            return "Product added to cart";
        } catch (Exception e) {
            return "Cart not found";
        }
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
        try {
            Product product = null;
            Cart cart = null;

            try {
                product = productService.getProductById(productId);
            } catch (Exception e) {
                return "Product not found";
            }

            try {
                cart = cartService.getCartByUserId(userId);
            } catch (Exception e) {
                return "Cart is empty";
            }

            cartService.deleteProductFromCart(cart.getId(), product);
            return "Product deleted from cart";
        } catch (Exception e) {
            return "Cart is empty";
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId The unique identifier of the user.
     * @return HTTP status indicating success.
     */
    @DeleteMapping("/delete/{userId}")
    public String deleteUserById(@PathVariable UUID userId) {
        try {
            userService.deleteUserById(userId);
            return "User deleted successfully";
        } catch (Exception e) {
            return "User not found";
        }
    }
}
