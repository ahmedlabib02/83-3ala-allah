package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class ProductService extends MainService<Product> {

     private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        if(product==null)
            throw new IllegalArgumentException("Product cannot be null");
        return productRepository.addProduct(product);
    }

    public ArrayList<Product> getProducts() {
        return productRepository.getProducts();
    }

    public Product getProductById(UUID productId) {
        if(productId ==null)
            throw new IllegalArgumentException("Product ID cannot be null");
        Product product = productRepository.getProductById(productId);
        if(product==null)
            throw new IllegalArgumentException("Product not found");
        return product;
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        if(productId == null)
            throw new IllegalArgumentException("Product ID cannot be null");
        if(newName.isEmpty())
            throw new IllegalArgumentException("Product name cannot be empty");
        Product product = productRepository.getProductById(productId);
        if(product==null)
            throw new IllegalArgumentException("Product not found");
        return productRepository.updateProduct(productId,newName,newPrice);
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        productRepository.applyDiscount(discount,productIds);
    }

    public void deleteProductById(UUID productId) {
        if(productId==null)
            throw new IllegalArgumentException("Product ID cannot be null");
        for(Product product : productRepository.getProducts()){
            if(product.getId().equals(productId)){
                productRepository.deleteProductById(productId);
                return;
            }
        }
        throw new IllegalArgumentException("Product not found");
    }

}
