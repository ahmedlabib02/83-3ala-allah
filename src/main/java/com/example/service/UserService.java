package com.example.service;

import com.example.model.Order;
import com.example.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class UserService extends MainService<User> {

    /**
     * To add a new User to our system
     * */
    public User addUser(User user) {
        return null;
    }

    /**
     * To get all the Users present in our system.
     * */
    public ArrayList<User> getUsers() {
        return null;
    }

    /**
     * To get a specific user by passing his/her ID.
     * */
    public User getUserById(int id) {
        return null;
    }

    /**
     * To get all the orders that were created by the user.
     * It takes the user ID as an input
     * */
    public List<Order> getOrdersByUserById(UUID userId) {
        return null;
    }

    /**
     * The user checks out his cart by creating. The user should empty
     * his cart and calculate everything to his order and add the new orders.
     * It should call methods from CartService.
     * */
    public void addOrderToUser(UUID userId) {

    }

    /**
     * This method should empty the cart of the user from the products
     * present inside. It should call methods from CartService.
     * */
    public void emptyCart(UUID userId) {

    }


    /**
     * To remove a specific order from the list of orders of the user.
     * */
    public void removeOrderFromUser(UUID userId) {

    }

    /**
     * To delete a specific user by passing his ID.
     * */
    public void deleteUserById(UUID userId) {

    }

}
