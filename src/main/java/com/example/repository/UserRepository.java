package com.example.repository;

import com.example.model.Order;
import com.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class UserRepository extends MainRepository<User> {

    public UserRepository() {}

    @Override
    protected String getDataPath() {
        return "";
    }

    @Override
    protected Class<User[]> getArrayType() {
        return null;
    }

    /**
    * Retrieve all users from the JSON file.
    * */
    public ArrayList<User> getUsers() {
        return null;
    }

    /**
    * Fetch a specific user from the JSON file by their unique ID.
    * @param userId user id
    * */
    public User getUserById(UUID userId) {
        return null;
    }

    /**
     * Adds a new user to the users JSON file.
     * @param user
     * */
    public User addUser(User user) {
        return null;
    }

    /**
     * Retrieve all orders for a given user ID.
     * */
    public List<Order> getOrdersByUserId(UUID userId) {
        return null;
    }

    /**
     * Lets the User add order.
     * */
    public void addOrderToUser(UUID userId, Order order) {

    }

    /**
     * Let the user remove one of his/her orders.
     * */
    public void removeOrderFromUser(UUID userId, Order order) {

    }

    /**
     * Delete a user by passing his/her ID.
     * */
    public void deleteUserById(UUID userId) {

    }

}
