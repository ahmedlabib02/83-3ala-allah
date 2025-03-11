package com.example.repository;

import com.example.model.Product;
import com.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class ProductRepository extends MainRepository<Product> {

    public ProductRepository() {

    }
    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/products.json";
    }

    @Override
    protected Class<Product[]> getArrayType() {
        return Product[].class;
    }

    public Product addProduct(Product product) {
        if (product == null) {
            System.out.println("Error: Product cannot be null!");
            return null;
        }
        if (getProductById(product.getId()) != null) {
            System.out.println("Product Already Exists!");
            return null;
        } else {
            System.out.println("Product Added!");
            save(product);
        }
        return product;
    }

    public ArrayList<Product> getProducts() {
        return findAll();
    }

    public Product getProductById(UUID productId) {
        for(Product product : findAll()){
            if(product.getId().equals(productId)){
                return product;
            }
        }
        return null;
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        if (productId == null || newName == null || newName.trim().isEmpty() || newPrice < 0) {
            System.out.println("Invalid input for product update!");
            return null;
        }

        ArrayList<Product> products = findAll();

        for (Product product : products) {
            if (product.getId().equals(productId)) {
                product.setName(newName);
                product.setPrice(newPrice);
                overrideData(products);
                System.out.println("Product updated successfully!");
                return product;
            }
        }

        System.out.println("Product does not exist!");
        return null;
    }

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        ArrayList<Product> products = findAll();

        for (Product product : products) {
            if (productIds.contains(product.getId())) {
                double newPrice = product.getPrice() - (product.getPrice() * discount / 100);
                product.setPrice(newPrice);
            }
        }
        overrideData(products);
        System.out.println("Discount applied successfully!");
    }


    public void deleteProductById(UUID productId) {
        ArrayList<Product> products = findAll();

        if (products.removeIf(product -> product.getId().equals(productId))) {
            overrideData(products);
            System.out.println("Product deleted successfully!");
        } else {
            System.out.println("Product does not exist!");
        }
    }

}
