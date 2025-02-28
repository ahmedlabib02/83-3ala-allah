package com.example.service;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class UserService extends MainService<User> {

    private final UserRepository userRepository;
    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public UserService(UserRepository userRepository, CartService cartService, ProductService productService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.productService = productService;
    }

    /**
     * Adds a new user to the system.
     *
     * @param user The user to be added.
     * @return The created user.
     */
    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    /**
     * Retrieves all users in the system.
     *
     * @return A list of all users.
     */
    public ArrayList<User> getUsers() {
        return userRepository.getUsers();
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The ID of the user.
     * @return The user object if found.
     */
    public User getUserById(UUID userId) {
        return userRepository.getUserById(userId);
    }

    /**
     * Retrieves all orders associated with a given user.
     *
     * @param userId The ID of the user.
     * @return A list of orders placed by the user.
     */
    public List<Order> getOrdersByUserById(UUID userId) {
        return userRepository.getOrdersByUserId(userId);
    }

    /**
     * Creates a new order for a user by checking out their cart.
     * The order total is calculated based on the products in the cart.
     * The user's cart is emptied after checkout.
     *
     * @param userId The ID of the user checking out.
     */
    public void addOrderToUser(UUID userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null || cart.getProducts().isEmpty()) {
            throw new IllegalStateException("Cart is empty or does not exist.");
        }

        double totalCost = cart.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(totalCost);

        // Empty the cart after creating an order
        emptyCart(userId);

        userRepository.addOrderToUser(userId, order);
    }

    /**
     * Empties the cart of a user by removing all products.
     *
     * @param userId The ID of the user whose cart should be emptied.
     */
    public void emptyCart(UUID userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart != null) {
            cart.getProducts().forEach(product -> productService.deleteProductById(product.getId()));
            cart.setProducts(new ArrayList<>());
        }
    }

    /**
     * Removes a specific order from a user's order history.
     *
     * @param userId  The ID of the user.
     * @param orderId The ID of the order to be removed.
     */
    public void removeOrderFromUser(UUID userId, UUID orderId) {
        userRepository.removeOrderFromUser(userId, orderId);
    }

    /**
     * Deletes a user from the system by their ID.
     *
     * @param userId The ID of the user to be deleted.
     */
    public void deleteUserById(UUID userId) {
        userRepository.deleteUserById(userId);
    }
}
