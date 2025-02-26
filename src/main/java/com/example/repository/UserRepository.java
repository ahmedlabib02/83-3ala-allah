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
        return "src/main/resources/data/users.json";
    }

    @Override
    protected Class<User[]> getArrayType() {
        return User[].class;
    }

    /**
    * Retrieve all users from the JSON file.
    * */
    public ArrayList<User> getUsers() {
        return findAll();
    }

    /**
    * Fetch a specific user from the JSON file by their unique ID.
    * @param userId user id
    * */
    public User getUserById(UUID userId) {
        ArrayList<User> users = findAll();
        boolean found = false;

        for (User user : users) {
            if (user.getId().equals(userId)) {
                System.out.println("User found successfully");
                return user;
            }
        }

        System.out.println("User does not exist");
        return null;
    }

    /**
     * Adds a new user to the users JSON file.
     * @param user
     * */
    public User addUser(User user) {
        if (getUserById(user.getId()) != null) {
            System.out.println("User already exists");
        }
        else {
            save(user);
            System.out.println("New user created successfully");
        }

        return user;
    }

    /**
     * Retrieve all orders for a given user ID.
     * */
    public List<Order> getOrdersByUserId(UUID userId) {
        User user = getUserById(userId);
        return user.getOrders();
    }

    /**
     * Lets the User add order.
     * */
    public void addOrderToUser(UUID userId, Order order) {
        List<Order> orders = getOrdersByUserId(userId);

        boolean found = false;

        for (Order o : orders) {
            if (o.getId().equals(order.getId())) {
                found = true;
            }
        }

        if (found) {
            System.out.println("Order already exists");
        }
        else {
            orders.add(order);
            System.out.println("Order added successfully");
        }
    }

    /**
     * Let the user remove one of his/her orders.
     * */
    public void removeOrderFromUser(UUID userId, UUID orderId) {
        List<Order> orders = getOrdersByUserId(userId);

        boolean found = false;

        for (Order o : orders) {
            if (o.getId().equals(orderId)) {
                orders.remove(o);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Order does not exist");
        }
        else {
            System.out.println("Order deleted successfully");
        }
    }

    /**
     * Delete a user by passing his/her ID.
     * */
    public void deleteUserById(UUID userId) {
        ArrayList<User> users = findAll();
        boolean found = false;

        for (User user : users) {
            if (user.getId().equals(userId)) {
                users.remove(user);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("User does not exist");
        }
        else {
            overrideData(users);
            System.out.println("User deleted successfully");
        }
    }



}
