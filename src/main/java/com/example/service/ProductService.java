package com.example.service;

import com.example.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class ProductService {

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

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {}

    public void deleteProductById(UUID productId) {}

}
