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

    UserRepository userRepository;
    CartService cartService;

    @Autowired
    public UserService(UserRepository userRepository, CartService cartService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    /**
     * To add a new User to our system
     * */
    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    /**
     * To get all the Users present in our system.
     * */
    public ArrayList<User> getUsers() {
        return userRepository.getUsers();
    }

    /**
     * To get a specific user by passing his/her ID.
     * */
    public User getUserById(UUID userId) {
        return userRepository.getUserById(userId);
    }

    /**
     * To get all the orders that were created by the user.
     * It takes the user ID as an input
     * */
    public List<Order> getOrdersByUserById(UUID userId) {
        return userRepository.getOrdersByUserId(userId);
    }

    /**
     * The user checks out his cart by creating a new order. The user should empty
     * his cart and calculate everything to his order and add the new orders.
     * It should call methods from CartService.
     * */
    public void addOrderToUser(UUID userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = new Order();
        double totalCost = 0;

        ArrayList<Product> products = new ArrayList<>(cart.getProducts());

        // Calculate the total cost of the order
        for (Product product : products) {
            totalCost += product.getPrice();
        }

        order.setTotalPrice(totalCost);

        // Empty the cart
        this.emptyCart(userId);

        // Add the new order
        userRepository.addOrderToUser(userId, order);
    }

    /**
     * This method should empty the cart of the user from the products
     * present inside. It should call methods from CartService.
     * */
    public void emptyCart(UUID userId) {
        Cart cart = cartService.getCartByUserId(userId);
        cart.setProducts(new ArrayList<Product>());
    }


    /**
     * To remove a specific order from the list of orders of the user.
     * */
    public void removeOrderFromUser(UUID userId, UUID orderId) {
        userRepository.removeOrderFromUser(userId, orderId);
    }

    /**
     * To delete a specific user by passing his ID.
     * */
    public void deleteUserById(UUID userId) {
        userRepository.deleteUserById(userId);
    }

}
