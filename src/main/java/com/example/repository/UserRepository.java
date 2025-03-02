package com.example.repository;

import com.example.model.Order;
import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class UserRepository extends MainRepository<User> {

    @Autowired
    OrderRepository orderRepository;

    public UserRepository() {}

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/users.json";
    }

    @Override
    protected Class<User[]> getArrayType() {
        return User[].class;
    }

    /**
     * Retrieves all users from the data source.
     *
     * @return A list of all users.
     */
    public ArrayList<User> getUsers() {
        return findAll();
    }

    /**
     * Fetches a user by their unique ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user object if found, otherwise null.
     */
    public User getUserById(UUID userId) {
        for (User user : findAll()) {
            if (user.getId().equals(userId)) {
                System.out.println("User found successfully");
                return user;
            }
        }
        System.out.println("User does not exist");
        return null;
    }

    /**
     * Adds a new user to the system if they do not already exist.
     *
     * @param user The user to be added.
     * @return The added user.
     */
    public User addUser(User user) {
        if (getUserById(user.getId()) != null) {
            System.out.println("User already exists");
        } else {
            save(user);
            System.out.println("New user created successfully");
        }
        return user;
    }

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of orders belonging to the user, or an empty list if none exist.
     */
    public List<Order> getOrdersByUserId(UUID userId) {
        User user = getUserById(userId);
        return (user != null) ? user.getOrders() : new ArrayList<>();
    }

    /**
     * Adds a new order to a user's order list.
     *
     * @param userId The ID of the user.
     * @param order  The order to be added.
     */
    public void addOrderToUser(UUID userId, Order order) {
        List<Order> orders = getOrdersByUserId(userId);

        if (orders.stream().anyMatch(o -> o.getId().equals(order.getId()))) {
            System.out.println("Order already exists");
        } else {
            // Add order to the database
            orders.add(order);

            // Update user database
            ArrayList<User> users = getUsers();

            // Iterate over the list of users & Update the order list of specified user id
            for (User user : users) {
                if (user.getId().equals(userId)) {
                    user.setOrders(orders);
                    break;
                }
            }

            // Update the JSON file
            overrideData(users);

            System.out.println("Order added successfully");
        }
    }

    /**
     * Removes a specific order from a user's order list.
     *
     * @param userId  The ID of the user.
     * @param orderId The ID of the order to be removed.
     */
    public void removeOrderFromUser(UUID userId, UUID orderId) {
        List<Order> orders = getOrdersByUserId(userId);

        if (orders.removeIf(order -> order.getId().equals(orderId))) {
            // Get the list of users
            ArrayList<User> users = getUsers();

            // Iterate over the list of users & Update the order list of specified user id
            for (User user : users) {
                if (user.getId().equals(userId)) {
                    user.setOrders(orders);
                    break;
                }
            }

            // Update the JSON file
            overrideData(users);

            System.out.println("Order deleted successfully");
        } else {
            System.out.println("Order does not exist");
        }
    }

    /**
     * Deletes a user from the system.
     *
     * @param userId The ID of the user to be deleted.
     */
    public void deleteUserById(UUID userId) {
        ArrayList<User> users = findAll();

        if (users.removeIf(user -> user.getId().equals(userId))) {
            overrideData(users);
            System.out.println("User deleted successfully");
        } else {
            System.out.println("User does not exist");
        }
    }
}
