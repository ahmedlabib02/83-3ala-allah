package com.example.repository;

import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class ProductRepository {

    public ProductRepository() {

    }

    public Product addProduct(Product product) {
        return null;
    }

    public ArrayList<Product> getProducts() {
        return null;
    }

    public Product getProductById(UUID productId) {
        return null;
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        return null;
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {

    }

    public void deleteProductById(UUID productId) {

    }

}
