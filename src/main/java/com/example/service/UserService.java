package com.example.service;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.CartRepository;
import com.example.repository.OrderRepository;
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
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Autowired
    public UserService(UserRepository userRepository, CartService cartService, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.orderRepository = orderRepository;
    }

    /**
     * Adds a new user to the system.
     *
     * @param user The user to be added.
     * @return The created user.
     */
    public User addUser(User user) {
        if (user != null) {
            if (userRepository.getUserById(user.getId()) != null) {
                throw new IllegalArgumentException("user already exists");
            } else {
                // Add a cart to the new user
                Cart cart = new Cart(UUID.randomUUID(), user.getId(), new ArrayList<>());

                // Add cart to the repository
                cartService.addCart(cart);

                // Add new user to the repository
                return userRepository.addUser(user);
            }
        } else {
            throw new IllegalArgumentException("user is null");
        }
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
        if (userRepository.getUserById(userId) != null) {
            return userRepository.getUserById(userId);
        } else {
            throw new IllegalArgumentException("user not found");
        }
    }

    /**
     * Retrieves all orders associated with a given user.
     *
     * @param userId The ID of the user.
     * @return A list of orders placed by the user.
     */
    public List<Order> getOrdersByUserById(UUID userId) {
        if (userRepository.getUserById(userId) != null) {
            return userRepository.getOrdersByUserId(userId);
        } else {
            throw new IllegalArgumentException("user not found");
        }
    }

    /**
     * Creates a new order for a user by checking out their cart.
     * The order total is calculated based on the products in the cart.
     * The user's cart is emptied after checkout.
     *
     * @param userId The ID of the user checking out.
     */
    public void addOrderToUser(UUID userId) {
        if (userRepository.getUserById(userId) != null) {
            // Get cart of the user
            Cart cart = cartService.getCartByUserId(userId);

            // If user did not have a cart then create a cart for him/her
            if (cart == null) {
                cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
                cartService.addCart(cart);
            }

            if (cart.getProducts().isEmpty()) {
                throw new IllegalStateException("Cart is empty");
            }

            // Calculate the total cost of products
            double totalCost = cart.getProducts().stream()
                    .mapToDouble(Product::getPrice)
                    .sum();

            // Update Order Repository
            Order order = new Order(UUID.randomUUID(), userId, totalCost, cart.getProducts());
            orderRepository.addOrder(order);

            // Empty the cart after creating an order
            emptyCart(userId);

            // Update User Repository
            userRepository.addOrderToUser(userId, order);
        } else {
            throw new IllegalArgumentException("user not found");
        }
    }

    /**
     * Empties the cart of a user by removing all products.
     *
     * @param userId The ID of the user whose cart should be emptied.
     */
    public void emptyCart(UUID userId) {
        if (userRepository.getUserById(userId) != null) {
            Cart cart = cartService.getCartByUserId(userId);

            // If user did not have a cart then create a cart for him/her
            if (cart == null) {
                cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
                cartService.addCart(cart);
            }

            List<Product> products = cart.getProducts();

            for (Product product : products) {
                cartService.deleteProductFromCart(cart.getId(), product);
            }

        } else {
            throw new IllegalArgumentException("user not found");
        }
    }

    /**
     * Removes a specific order from a user's order history.
     *
     * @param userId  The ID of the user.
     * @param orderId The ID of the order to be removed.
     */
    public void removeOrderFromUser(UUID userId, UUID orderId) {
        if (userRepository.getUserById(userId) != null) {
            if (orderRepository.getOrderById(orderId) != null) {
                if (orderRepository.getOrderById(orderId).getUserId().equals(userId)) {
                    userRepository.removeOrderFromUser(userId, orderId);
                    orderRepository.deleteOrderById(orderId);
                } else {
                    throw new IllegalArgumentException("user not assigned to that order");
                }
            }
            else {
                throw new IllegalArgumentException("order not found");
            }
        } else {
            throw new IllegalArgumentException("user not found");
        }
    }

    /**
     * Deletes a user from the system by their ID.
     *
     * @param userId The ID of the user to be deleted.
     */
    public void deleteUserById(UUID userId) {
        if (userRepository.getUserById(userId) != null) {
            userRepository.deleteUserById(userId);
        } else {
            throw new IllegalArgumentException("user not found");
        }
    }
}
